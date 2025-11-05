"""
Streamlit UI to pre-process PDF documents and a Predefined Q&A set and compute their vector embeddings.
Author: PratulyaShah (pbimalshah@deloitte.com)
"""

import os
import pickle
import io
import re
from datetime import datetime as dt
from urllib.request import urlretrieve
from distutils.dir_util import copy_tree

import numpy as np
import pandas as pd
F
import pdfplumber
import fitz
import cv2
import pytesseract

import openai
import tiktoken
import  transformers 
import streamlit as st

import warnings
warnings.filterwarnings(action="ignore", category=DeprecationWarning)
warnings.simplefilter(action='ignore', category=FutureWarning)
warnings.warn("deprecated", DeprecationWarning)


# Download PDFs from the given URLs and save them
def download_pdfs(df: pd.DataFrame, domain: str, save_path: str) -> bool:
    """
    Input: Dataframe containing filenames and their download URLs
    Output: Downloads files to a folder and returns the absolute path to that folder.
    """
    df.drop_duplicates(subset=["url"], inplace=True)
    num_of_errors = 0
    progress_bar = st.progress(0, text="Downloading PDFs")
    for idx, row in df.iterrows():
        cur_file_name = row["filename"]
        cur_file_path = os.path.join(save_path, "Input_Documents", domain, cur_file_name)
        
        try:
            urlretrieve(url=str(row["url"]), filename=cur_file_path)
            print(f"""\t{row["filename"]} downloaded!""")
        except Exception as e:
            print(f"""Error occured in downloading {row["filename"]}! Full Message:\n""" + str(e))
            num_of_errors += 1
        progress_bar.progress(value=round(((idx+1)/df.shape[0])*100), text="Downloading PDFs")
    if num_of_errors < df.shape[0]:
        return os.path.abspath(f"{save_path}/Input_Documents/{domain}")
    else:
        return None
        

# Perform OCR on all images in a page of a PDF using PyMuPDF and Tesseract, and return the data in a structured format
def convert_images_in_pdfs_to_text(input_path: str, filename: str, page_num: int):
    """
    Input: Path of PDF file, its filename and the page number to extract the images from.
    Output: List of dictionaries. Each dictionary contains the text extracted from a single image on the page along with id fields.
    """
    pdf_file = fitz.open(input_path)
    page = pdf_file[(page_num-1)]
    row_list = []
    for image_cnt, image in enumerate(page.get_images(), start=1):
        cur_xref = image[0]
        # Extract and convert image to OpenCV format
        image_array = np.asarray(bytearray(pdf_file.extract_image(cur_xref)["image"]), dtype=np.uint8)
        image_obj = cv2.imdecode(image_array, flags=0)
        # Parse text in image using Tesseract OCR
        try:
            text = pytesseract.image_to_string(image_obj)
            row = {"filename":filename, "page":page_num,
                   "content_type":"image", "content_type_id":"image_"+str(image_cnt), 
                   "content":text}
            row_list.append(row)
        except TypeError as e:
            print(f"Error is extracting text from image-{image_cnt} on page-{page_num}! Error:\n\t{e}")
    return row_list


# Parse PDFs page-wise
def parse_pdfs_to_dataframe_pagewise(path: str, remove_last: bool=True):
    """
    Input: Path to a PDF file.
    Output: Dataframe containing the text extracted from plain text, tables and images in the PDF along with identifiers for each.
    """
    pdf = pdfplumber.open(path)
    # table bbox for filter
    def not_within_bboxes(obj):
        def obj_in_bbox(_bbox):
            v_mid = (obj["top"] + obj["bottom"]) / 2
            h_mid = (obj["x0"] + obj["x1"]) / 2
            x0, top, x1, bottom = _bbox
            return (h_mid >= x0) and (h_mid < x1) and (v_mid >= top) and (v_mid < bottom)
        return not any(obj_in_bbox(__bbox) for __bbox in bboxes)
    
    basename = os.path.basename(path).split('/')[-1] #Extract the name of the file from the path
    page_df_list = []

    # Scrape through each page of pdf partitioned by page
    # First ignore all content w/in table based on filter
    # Append table values as list-of-lists at the end of each partition
    # Each list element in LoL corresponds to a row in the table
    for page in pdf.pages:
        bboxes = [table.bbox \
                  for table in page.find_tables(table_settings={"vertical_strategy": "lines", "horizontal_strategy": "lines"})]
        
        # Strip away any unwanted newlines at beginning and end of text
        # Remove page number from footer
        # Drop empty string or white space string and replace them with nan
        page_text = page.filter(not_within_bboxes).extract_text().strip()

        # Drop page number
        if remove_last is True:
            if page_text != "":
                pattern = re.compile(r"\n")
                if pattern.finditer(page_text) is not None and len(pattern.findall(page_text)) > 0:
                    *_, s = pattern.finditer(page_text)
                    page_text = page_text[:s.span()[0]]
        
        tables = page.extract_tables()
        if len(tables)>0:
            # Extract tables from the page (when they exist)
            for table_cnt, table in enumerate(page.extract_tables(), start=1):
                page_table = str(table)
                page_content = f"Page Text:\n{page_text}\n\nTable-{table_cnt}:\n{page_table}"
                new_row_table = {"filename":basename, "page": int(page.page_number), 
                                 "content_type": "table", "content_type_id":"table_"+str(table_cnt), "content": page_content}
                page_df_list.append(new_row_table)
        else:
            new_row_text = {"filename":basename, "page": int(page.page_number), 
                            "content_type": "text", "content_type_id": "text", "content": page_text}
            page_df_list.append(new_row_text)
            
        # Extract images from the page (if they exist)
        if page.images != []:
            image_content_list = convert_images_in_pdfs_to_text(input_path=path, filename=basename, page_num=page.page_number)
            page_df_list.extend(image_content_list)

    all_pages_df = pd.DataFrame(page_df_list)
    all_pages_df["content"].replace('', np.nan, inplace=True)
    all_pages_df.dropna(subset=["content"], inplace=True)
    return all_pages_df


# Function to count the number of tokens in a given text
def num_tokens_from_messages(content):
    """
    Input: Text content whose tokens are to be calculated
    Returns the number of tokens used by a list of messages.
    """
    #tokenizer = AutoTokenizer.from_pretrained('BAAI/bge-large-en-v1.5')
    encoded_input = TOKENIZER(content)
    #tokenizertest = AutoTokenizer.from_pretrained('BAAI/bge-large-en-v1.5')
    #print("Length of Sentence: ", )
    return len(encoded_input.input_ids)
    '''
    print("Token?: ", len(encoded_input))
    try:
        encoding = tiktoken.encoding_for_model(model)
    except KeyError:
        encoding = tiktoken.get_encoding("cl100k_base")
    if model == "gpt-3.5-turbo-0301":  # note: future models may deviate from this
        num_tokens = 0
        num_tokens += len(encoding.encode(content))       
        return num_tokens
    else:
        raise NotImplementedError(f"""num_tokens_from_messages() is not presently implemented for {model} model.\n
                                      See https://github.com/openai/openai-python/blob/main/chatml.md for information on how messages are converted to tokens.""")
    '''

# Function to process parsed PDF dataframe
def process_parsed_df(df: pd.DataFrame):
    """
    Input: Dataframe of text extracted by page from all PDFs.
    Output: Dataframe with cleaned-up extracted text and token count of extracted text from each page for logging.
    """
    df["page_str"] = df["page"].astype(str) #Convert the datatype of the page
    df["filename"] = df["filename"].apply(lambda x: x.strip().replace(".pdf", '').replace("_", "-").replace(" ", "-")) #Strip the file extension and replace spaces/ underscores with hyphens from the file name
    df["doc_index"] = df["filename"] + '_' + df["page_str"] + '_' + df["content_type_id"] #Create a unique index for each row
    df["content"] = df["content"].apply(lambda x: x.replace('\n', ' ')) #Replace EOL characters in text conent with spaces
    df.dropna(axis=0, inplace=True) #Drop empty rows
    df["tokens"] = df["content"].apply(lambda x: num_tokens_from_messages(str(x))) #Calculate the token usage of the text content of each row
    df = df.loc[df["tokens"]!=0, :]
    df['order'] = ""
    #Break down tokens past 512 so that BAA embedder can embed.
    while (df['tokens'] > 512).any():
      mask = (df['tokens'] > 512)
      df_valid = df[mask]
      df['content2'] = ''
      #Split the content where token length is too large.
      df.loc[mask, 'content2'] = df_valid['content'].apply(lambda x: x[len(x)//2:])
      df.loc[mask, 'content'] = df_valid['content'].apply(lambda x: x[0:len(x)//2])
      new_df = df.copy()
      new_df = df.loc[df['content2'] != ''] 
      df = df.drop(columns=['content2'])
      new_df = new_df.drop(columns=['content'])
      new_df = new_df.rename(columns={"content2": "content"})
      #Ordering system so that split files are correctly recombined when resorted.
      df.loc[mask, 'order'] += "0"
      new_df.loc[mask,'order'] += "1"
      df = pd.concat([df,new_df],ignore_index=True)
      df["tokens"] = df["content"].apply(lambda x: num_tokens_from_messages(str(x))) #Calculate the token usage of the text content of each row
    df = df.sort_values(by=['filename','page','order'])
    df.reset_index(inplace=True)
    df = df.loc[:, ["doc_index", "content", "tokens", "url"]] #Select only columns that are necessary for computing embeddings
    return df
     

# Function to process parsed Q&A dataframe
def process_qa_df(df: pd.DataFrame):
    """
    Input: Dataframe of predefined Q&A set.
    Output: Processed dataframe with cleaned-up answers and token count of answers for logging.
    """
    df.rename(columns={"#":"question_num", "Q":"question", "A":"answer"}, inplace=True)
    df["answer"] = df["answer"].apply(lambda x: x.replace(' -', '\n-').replace('.  ', '.\n')) #Replace EOL characters in text conent with spaces
    df.dropna(axis=0, inplace=True, ignore_index=True) #Drop empty rows
    df["tokens"] = df["answer"].apply(lambda x: num_tokens_from_messages(str(x))) #Calculate the token usage of the text content of each row
    df = df.loc[df["tokens"]!=0, :]
    df["question_no"]= df.index + 1
    df = df.loc[:, ["question_no", "question", "answer", "tokens"]] #Select only columns that are necessary for computing embeddings
    df.set_index(["question_no"], inplace=True) #Set an index to the dataframe for faster processing
    return df


# Compute embedding for a text string using an embedding model (default: 'text-embedding-ada-002').
def get_embedding(text: str, model: str) -> list[float]:
    """
    Input: A text string.
    Output: The word embedding for the text string using the model defined in EMBEDDING_MODEL (default: 'text-embedding-ada-002').
    """
    result = openai.Embedding.create(model=model, input=text)
    return result["data"][0]["embedding"]


# Compute embeddings using the Universal Sentence Encoder
def get_universal_embedding(text: str, model) -> list[float]:
    """
    Input: A text string.
    Output: The word embedding for the text string using the Universal Sentence Encoder Model.
    """
    text = [text]
    embedding = model(text)
    return embedding[0].numpy().tolist()


# Compute embedding for the whole document divided into chunks
def compute_doc_embeddings(chunks_df: pd.DataFrame, model: str) -> dict[tuple[str, str], list[float]]:
    """
    Input: A dataframe that contains different chunks of the PDF document. These chunks can be para-wise (semantically delimited) or page-delimited.
    Output: A dictionary with the file name+page numbers as the key and the OCR content of those pages as the values.
    """
    return {idx: get_embedding(text=row.content, model=model) for idx, row in chunks_df.iterrows()}


# Compute embedding for the whole document divided into chunks using Universal Sentence Encoder
def compute_doc_universal_embeddings(chunks_df: pd.DataFrame) -> dict[tuple[str, str], list[float]]:
    """
    Input: A dataframe that contains different chunks of the PDF document. These chunks can be para-wise (semantically delimited) or page-delimited.
    Output: A dictionary with the file name+page numbers as the key and the OCR content of those pages as the values.
    """
    return {idx: get_universal_embedding(row.content) for idx, row in chunks_df.iterrows()}


# Compute embeddings for predefined QA set
def compute_predefined_qa_embeddings(qa_df: pd.DataFrame, model: str) -> dict[str: list[float]]:
    """
    Input: A dataframe that contains predefined questions and their ideal (human) answers
    Output: A dictionary with the question number as the key and the embedding of the question as the value
    """
    return {idx: get_embedding(row.question, model=model) for idx, row in qa_df.iterrows()}


# Compute embeddings for predefined QA set using Universal Sentence Encoder
def compute_predefined_qa_universal_embeddings(qa_df: pd.DataFrame) -> dict[str: list[float]]:
    """
    Input: A dataframe that contains predefined questions and their ideal (human) answers
    Output: A dictionary with the question number as the key and the embedding of the question as the value
    """
    return {idx: get_universal_embedding(row.question) for idx, row in qa_df.iterrows()}


# Calculate the vector similarity between two text strings
def vector_similarity(x: list[float], y: list[float]) -> float:
    """
    Input: Two lists x & y. Each list contains the embeddings of a text string.
    Output: Dot product (a float) of x & y (parsed as NumPy arrays).
            NOTE: Since OpenAI embeddings are normalized to length 1, the dot product is equivalent to the cosine similarity.
    """
    return np.dot(np.array(x), np.array(y))


# Define global variables
TOKENIZER = transformers.AutoTokenizer.from_pretrained('BAAI/bge-large-en-v1.5')
EMBEDDING_MODEL = "text-embedding-ada-002"
#ENTER YOUR API KEY HERE
openai.api_key = ""
save_path_policy = os.path.join(os.path.dirname(os.path.dirname(__file__)), 
                                "PolicyGPT", "Streamlit UI")
save_path_case = os.path.join(os.path.dirname(os.path.dirname(__file__)), 
                              "Case Co-Pilot", "Streamlit UI")
save_path_general = os.path.join(os.path.dirname(os.path.dirname(__file__)),"Process PDFs")

# Streamlit execution
st.title("Process PDFs for LLMs")
st.header(":violet[Overview]")
justify_html = "<style> p {text-align: justify;}</style>"
st.markdown(justify_html, unsafe_allow_html=True)
st.write("Helps users to extract and transfrom data from their own, private PDF files so that they can be loaded into Large Language Models.\
          Textual content from plain text, images and tables in the PDFs is extracted, divided into chunks which are then embedded into vectors.\
          A table with chunks of the document (.csv) and an embeddings (.pkl) file are generated which can then be loaded into the AskMe.AI LLM Engine.")
st.write("")
st.write("")

# Accept domain (folder name to save files) from the user
domain = st.text_input("Please specify a domain name for this use-case.\
                        A folder of this name is created in the current directory to save the output.")
st.write()

if domain!="":
    start_time = dt.now()
    if "start_time" not in st.session_state:
        st.session_state["start_time"] = start_time
    to_upload_excel = st.radio(label="Do you have Excel file to upload with filenames and urls?",
                               options=("None", "Yes", "No"))
    if to_upload_excel=="Yes":
        st.subheader(":violet[Excel File Upload]")
        st.write("Kindly upload an Excel file with two columns - 'filename' and 'url'.\
                The LLM engine refers to this file to populate references used in its answer.")
        excel_file = st.file_uploader(label="Upload your Excel file here ...", 
                                      type=["xlsx"], 
                                      accept_multiple_files=False)
        if excel_file is not None:
            if "filelist_df" not in st.session_state or st.session_state["filelist_df"] is None:
                st.session_state["filelist_df"] = pd.read_excel(excel_file)
            if not all([item in st.session_state["filelist_df"].columns for item in ["filename", "url"]]):
                st.write("Columns - 'filename' and/ or 'url' not found in Excel file! Please reupload the Excel file.")
                st.session_state["filelist_df"] = None
                st.experimental_rerun()
            st.write()
            st.subheader(":violet[Loading PDF files]")
            st.write()
            OPTION = st.radio(label="Do you want to download publicly accessible PDFs or upload your own?", 
                              options=("None", "Download", "Upload"))
    elif to_upload_excel=="No":
        OPTION = "Upload"
    else:
        OPTION = "None"

    if "OPTION" in globals():
        if OPTION!="None":
            if ("pdf_loaded" not in st.session_state or st.session_state["pdf_loaded"]==False):
                 # Create directories to store PDF files for Policy GPT
                if "Input_Documents" not in [dir for dir in os.listdir(save_path_general) 
                                             if os.path.isdir(os.path.join(save_path_general, dir))]:
                    os.mkdir(os.path.join(save_path_general, "Input_Documents"))
                if domain not in [dir for dir in os.listdir(os.path.join(save_path_general, "Input_Documents")) 
                                  if os.path.isdir(os.path.join(save_path_general, "Input_Documents", dir))]:
                    os.mkdir(os.path.join(save_path_general, "Input_Documents", domain))
                
                if OPTION=="Download":
                    print("\nDownloading documents .....")
                    folder = download_pdfs(df=st.session_state["filelist_df"], domain=domain, 
                                           save_path=save_path_general)
                    
                    if folder is None:
                        st.write("Download of all files failed. Please recheck the URLs in the Excel file uploaded.\nRestarting now ...")
                        st.session_state["pdf_loaded"] = False
                        st.experimental_rerun()
                    st.session_state["download_time"] = dt.now() - start_time
                    st.write(f"""**:green[Document download complete! Time taken: {st.session_state["download_time"]}]**""")
                    print(f"*** Document download complete! Time taken: {(dt.now() - start_time)} ***")

                    # Copy downloaded documents to Case Co-Pilot
                    copy_tree(folder, f"{save_path_general}/Input_Documents/{domain}")

                    st.session_state["pdf_loaded"] = True
                elif OPTION=="Upload":
                    pdf_files = st.file_uploader("Please upload your PDF files here ...",
                                                 type=[".pdf", ".PDF"],
                                                 accept_multiple_files=True)
                    if pdf_files!=[]:
                        if to_upload_excel=="No":
                            st.session_state["accept_url_header"] = st.radio("Do you want to enter a custom URL header?",
                                                                            options=("None", "Yes", "No"))
                            if st.session_state["accept_url_header"]!="None":
                                if st.session_state["accept_url_header"]=="Yes":
                                    url_header = st.text_input("Please enter a URL header.\
                                                                This URL header will be displayed in the references section of answers generated by AskMe.AI")
                                else:
                                    url_header = f"https://www.{domain.lower().replace(' ', '')}.gov/Documents/"
                                if url_header!='':
                                    with st.spinner(text="Generating Excel file with default urls ..."):
                                        filenames = []
                                        urls = []
                                        for pdf_file in pdf_files:
                                            filenames.append(pdf_file.name)
                                            urls.append(f"{url_header.strip('/')}/{pdf_file.name}")
                                        st.session_state["filelist_df"] = pd.DataFrame({"filename":filenames, "url":urls})
                                    st.session_state["accept_url_header"] = None
                                    st.write(":green[Excel file with dummy URLs generated!]")
                        if "filelist_df" in st.session_state and st.session_state["filelist_df"] is not None:
                            filenames = st.session_state["filelist_df"].loc[:, "filename"].to_list()
                            for pdf_file in pdf_files:
                                if pdf_file is not None:
                                    if pdf_file.name in filenames:
                                        folder = os.path.abspath(f"{save_path_general}/Input_Documents/{domain}")
                                        # Write valid PDF file to Policy GPT storage
                                        with open(os.path.join(folder, pdf_file.name), "wb") as file:
                                            file.write(pdf_file.getbuffer())
                                        # Write valid PDF file to Case Co-Pilot storage
                                        with open(os.path.join(f"{save_path_general}/Input_Documents/{domain}", pdf_file.name), "wb") as file:
                                            file.write(pdf_file.getbuffer())
                                        st.session_state["pdf_loaded"] = True
                                    else:
                                        print(f"{pdf_file.name} not found in Excel! This file will be skipped!")
                                        st.write(f":red[{pdf_file.name} does not exist in list of files provided in Excel! This file will be skipped!]")
                                else:
                                    continue
            else:
                if OPTION=="Download":
                    st.progress(value=100, text="Downloading PDFs")
                    st.write(f"""**:green[Document download complete! Time taken: {st.session_state["download_time"]}]**""")
                elif OPTION=="Upload":
                    pdf_files = st.file_uploader("Please upload your PDF files here ...",
                                                 type=[".pdf", ".PDF"],
                                                 accept_multiple_files=True)
                    if to_upload_excel=="No":
                        st.write(":green[Excel file with dummy URLs generated!]")
        
    if "pdf_loaded" in st.session_state and st.session_state["pdf_loaded"]==True:
        if "doc_df" not in st.session_state:
            # Parse PDFs page-wise into a dataframe
            pdf_list = [f for f in os.listdir(folder) if f.endswith(".pdf") or f.endswith(".PDF")]
            df_list = []
            parsing_start_time = dt.now()
            print("\nParsing PDFs .....")
            progress_bar = st.progress(0, text="PDF Parsing")
            for cnt, file in enumerate(pdf_list, start=1):
                file_path = os.path.join(folder, file)
                df_list.append(parse_pdfs_to_dataframe_pagewise(path=file_path))
                print(f"\tParsing for {file} complete!")
                progress_bar.progress(value=(round(cnt/len(pdf_list)*100)), text="PDF Parsing")
            all_files_df = pd.concat(df_list, axis=0, ignore_index=True)
            all_files_df = all_files_df.merge(st.session_state["filelist_df"], how="left", on=["filename"])
            all_files_df.to_csv(os.path.join(folder, "PDF_Parsing_Results.csv"), index=False,escapechar='\\') #Write parsing results to Policy GPT storage
            all_files_df.to_csv(os.path.join(f"{save_path_general}/Input_Documents/{domain}", "PDF_Parsing_Results.csv"), index=False,escapechar='\\') #Write parsing results to Case Co-Pilot storage
            if to_upload_excel=="No":
                st.session_state["filelist_df"].to_excel(os.path.join(folder, "PDF_List.xlsx"), index=False) #Write generated PDF_List Excel file to Policy GPT storage
                st.session_state["filelist_df"].to_excel(os.path.join(f"{save_path_general}/Input_Documents/{domain}", "PDF_List.xlsx"), index=False) #Write generated PDF_List Excel file to Case Co-Pilot storage
            st.session_state["parsing_time"] = dt.now() - parsing_start_time
            st.write(f"""**:green[All PDFs parsed! Time taken: {st.session_state["parsing_time"]}]**""")
            print(f"*** All PDFs parsed! Time taken: {(dt.now() - parsing_start_time)} ***")
                
            # Process parsed PDF dataframe
            print("Processing PDFs .....", end=" ")
            doc_df = process_parsed_df(df=all_files_df)
            st.session_state["doc_df"] = doc_df
            print("Complete!")
            st.write("**:green[Processing PDFs Complete]**")
        else:
            st.progress(value=100, text="PDF Parsing")
            st.write(f"""**:green[All PDFs parsed! Time taken: {st.session_state["parsing_time"]}]**""")
            st.write("**:green[Processing PDFs Complete]**")
            doc_df = st.session_state["doc_df"]
       
        # Read Predefined Q&A set
        st.write()
        predefined_qa = st.radio(label="Do you want to load a predefined Q&A set? To save files select Yes/ No", 
                                 options=("None", "Yes", "No"))
        if predefined_qa=="Yes":
            if "predefined_qa_df" not in st.session_state:
                st.subheader(":violet[Loading Predefined Q&A set]")
                st.write("Kindly upload an Excel file with the following columns - # (denoting serial number),\
                        Q (denoting question) and A (denoting answer)")
                predefined_qa_file = st.file_uploader(label="Upload your predefined Q&A Excel file here ...",
                                                      type=["xlsx"],
                                                      accept_multiple_files=False)
                st.session_state["predefined_qa_file_upload"] = predefined_qa_file
                if predefined_qa_file is not None:
                    predefined_qa_df = pd.read_excel(predefined_qa_file)
                    print("\nLoaded predefined Q&A set!")
                    if not all([item in predefined_qa_df.columns for item in ["#", "Q", "A"]]):
                        st.write("Columns - '#' and/ or 'Q' and/or 'A' not found in predefined Q&A Excel file!\
                                Skipping computations for predefined Q&A!")
                        print("Skipping computation for predefined Q&A set!")
                        predefined_qa = "No"
                    else:
                        print("Processing Predefined Q&A set .....", end=" ")
                        predefined_qa_df = process_qa_df(predefined_qa_df)
                        st.session_state["predefined_qa_df"] = predefined_qa_df
                        print("Complete!")
                        st.write("**:green[Predefined Q&A set processed!]**")
                    
                    if "predefined_qa_embeddings" not in st.session_state and predefined_qa=="Yes":
                        # Compute embeddings for Predefined Q&A set
                        predefined_embedding_start_time = dt.now()
                        print("*** Computing predefined Q&A embeddings .....", end=" ")
                        with st.spinner(text="Computing predefined Q&A embeddings ....."):
                            predefined_qa_embeddings = compute_predefined_qa_embeddings(predefined_qa_df, model=EMBEDDING_MODEL)
                        st.session_state["predefined_qa_embeddings"] = predefined_qa_embeddings
                        st.session_state["predefined_qa_embedding_time"] = dt.now() - predefined_embedding_start_time
                        st.write(f"""**:green[Predefined Q&A Embeddings Computed! Time taken: {st.session_state["predefined_qa_embedding_time"]}]**""")
                        print(f"Complete! Time taken: {(dt.now() - predefined_embedding_start_time)}")
                    elif "predefined_qa_embeddings" in st.session_state:
                        st.write(f"""**:green[Predefined Q&A Embeddings Computed! Time taken: {st.session_state["predefined_qa_embedding_time"]}]**""")
                        predefined_qa_embeddings = st.session_state["predefined_qa_embeddings"]
            else:
                st.subheader(":violet[Loading Predefined Q&A set]")
                st.write("Kindly upload an Excel file with the following columns - # (denoting serial number),\
                        Q (denoting question) and A (denoting answer)")
                predefined_qa_file = st.file_uploader("Upload your predefined Q&A Excel file here ...",
                                                    type=["xlsx"], 
                                                    accept_multiple_files=False)
                st.write("**:green[Predefined Q&A set processed!]**")
                st.write(f"""**:green[Predefined Q&A Embeddings Computed! Time taken: {st.session_state["predefined_qa_embedding_time"]}]**""")
                predefined_qa_df = st.session_state["predefined_qa_df"]
                predefined_qa_embeddings = st.session_state["predefined_qa_embeddings"]

        if predefined_qa!="None":
            if predefined_qa=="Yes" and st.session_state["predefined_qa_file_upload"] is None:
                pass
            else:
                # Create folder and save csv and embeddings files
                with st.spinner(text="Saving files ....."):
                    if "VectorDB" not in [dir for dir in os.listdir(save_path_general) 
                                          if os.path.isdir(os.path.abspath(f"{save_path_general}/{dir}"))]:
                        os.mkdir(os.path.abspath(f"{save_path_general}/VectorDB"))
                    vector_db_folder_policy = os.path.abspath(f"{save_path_general}/VectorDB/{domain}")
                    if domain not in [dir for dir in os.listdir(f"{save_path_general}/VectorDB")
                                      if os.path.isdir(os.path.abspath(f"{save_path_general}/VectorDB/{dir}"))]:
                        os.mkdir(vector_db_folder_policy)

                    if "VectorDB" not in [dir for dir in os.listdir(save_path_general) 
                                          if os.path.isdir(os.path.abspath(f"{save_path_general}/{dir}"))]:
                        os.mkdir(os.path.abspath(f"{save_path_general}/VectorDB"))
                    vector_db_folder_case = os.path.abspath(f"{save_path_general}/VectorDB/{domain}")
                    if domain not in [dir for dir in os.listdir(f"{save_path_general}/VectorDB")
                                      if os.path.isdir(os.path.abspath(f"{save_path_general}/VectorDB/{dir}"))]:
                        os.mkdir(vector_db_folder_case)

                    print(f"Saving data to {vector_db_folder_policy} .....")
                    # Write documents dataframe to csv and embeddings to a pickle file (OVERWRITES files)
                    doc_df.to_csv(os.path.join(vector_db_folder_policy, "Processed_PDFs.csv"),index=False,escapechar='\\')
                   # with open(os.path.join(vector_db_folder_policy, "Embeddings.pkl"), "wb") as file:
                   #     pickle.dump(doc_embeddings, file)
                    print("\tDocument data saved!")
                    # Write predefined Q&A dataframe to csv and embeddings to a pickle file (OVERWRITES files)
                    if predefined_qa=="Yes":
                        predefined_qa_df.to_csv(os.path.join(vector_db_folder_policy, "Predefined_QA.csv"), index=True)
                        with open(os.path.join(vector_db_folder_policy, "Predefined_QA.pkl"), "wb") as file:
                            pickle.dump(predefined_qa_embeddings, file)
                        print("\tPredefined Q&A data saved!")
                    st.write(f":green[Files saved succesfully for Policy Bot to {vector_db_folder_policy}!]")

                    print(f"Saving data to {vector_db_folder_case} .....")
                    # Write documents dataframe to csv and embeddings to a pickle file (OVERWRITES files)
                    doc_df.to_csv(os.path.join(vector_db_folder_case, "Processed_PDFs.csv"),index=False,escapechar='\\')
                   # with open(os.path.join(vector_db_folder_case, "Embeddings.pkl"), "wb") as file:
                    #    pickle.dump(doc_embeddings, file)
                    print("\tDocument data saved!")
                    # Write predefined Q&A dataframe to csv and embeddings to a pickle file (OVERWRITES files)
                    if predefined_qa=="Yes":
                        predefined_qa_df.to_csv(os.path.join(vector_db_folder_case, "Predefined_QA.csv"), index=True)
                        with open(os.path.join(vector_db_folder_case, "Predefined_QA.pkl"), "wb") as file:
                            pickle.dump(predefined_qa_embeddings, file)
                        print("\tPredefined Q&A data saved!")
                    st.write(f":green[Files saved succesfully for Case Co-Pilot to {vector_db_folder_case}!]")

                st.write()
                save_custom = st.radio(label="Do you want to keep a copy of the files on your own directories? Enter Yes/ No to stop execution", 
                                    options=("None", "Yes", "No"))
                if save_custom=="Yes":
                    st.subheader(":violet[Downloading Output Files]")
                    doc_csv = doc_df.to_csv(index=False,escapechar='\\').encode("utf-8")
                    st.download_button(label=":blue[Download processed PDFs dataframe]",
                                       data=doc_csv,
                                       file_name="Processed_PDFs.csv", mime="text/csv")
                    #embeddings_file = io.BytesIO()
                    #st.download_button(label=":blue[Download document embeddings]",
                     #                  data=embeddings_file, 
                      #                 file_name="Embeddings.pkl")
                    if predefined_qa=="Yes":
                        predefined_csv = predefined_qa_df.to_csv().encode("utf-8")
                        st.download_button(label=":blue[Download predefined Q&A dataframe]",
                                           data=predefined_csv,
                                           file_name="Predefined_QA.csv", mime="text/csv")
                        qa_embeddings_file = io.BytesIO()
                        pickle.dump(predefined_qa_embeddings, qa_embeddings_file)
                        st.download_button(label=":blue[Download predefined Q&A embeddings]",
                                           data=qa_embeddings_file, 
                                           file_name="Predefined_QA.pkl")
                    print(f"""\n******* DOCUMENT PROCESSING COMPLETE! Time taken: {(dt.now() - st.session_state["start_time"])} *******""")
                    st.write(f"""**:green[DOCUMENT PROCESSING COMPLETE! Time taken: {(dt.now() - st.session_state["start_time"])}]**""")
                    st.stop()
                elif save_custom=="No":
                    print(f"""\n******* DOCUMENT PROCESSING COMPLETE! Time taken: {(dt.now() - st.session_state["start_time"])} *******""")
                    st.write(f"""**:green[DOCUMENT PROCESSING COMPLETE! Time taken: {(dt.now() - st.session_state["start_time"])}]**""")
                    st.stop()
    else:
        if "filelist_df" in st.session_state and st.session_state["filelist_df"] is not None and OPTION=="Upload":
            st.write("**:red[No valid PDFs found! The names of the PDFs uploaded do not match those in the Excel file!]**")
