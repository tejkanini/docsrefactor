# Preprocess PDFs for LLMs

This folder contains a Python script that provides a simple user interface to download/ upload your own PDFs, extract textual (plain and in images) and tabular content from them, chunk this content page-wise, and then compute the vector embeddings for each of these document chunks. Creating vector embeddings of text in custom documents allows one to harness the immense potential of Large Language Models and Generative AI to provide honest, complete, and relevant answers to questions on these documents. This script is primarily designed to integrate with Deloitte's AskMe.AI and Case Co-Pilot Q&A Engines based on OpenAI GPT LLMs. While this tool can be used to process documents for consumption by other LLMs as well, integration with other LLM-based solutions has not been tested.

### Installation (First-Time)

1. Install Python 3.11 (any sub-version) and above from the [official website]: https://www.python.org/downloads/
2. Install Tesseract for your platform. Mac users can use Homebrew (In terminal type: `brew install tesseract`) using the Linux users can use the distro's built-in package manager (tesseract or tesseract-ocr depending on your distro). Windows users should follow the steps given below:
a. Download the Tesseract executable (.exe file) from [UB Mannheim](https://github.com/UB-Mannheim/Tesseract_Dokumentation).
b. Run this executable and use the default options to install Tesseract.
**IMPORTANT**: Please copy the default installation path for Tesseract in a text file.
c. In the Windows Start Search, type "Edit System Variables" and open the wizard to edit System Variables. Please enter your Deloitte credentials if prompted to do so.
d. In the wizard, click on the 'Environment Variables' button in the bottom-right.
e. In the pop-up, under the header 'System Variables' look for the variable name 'Path' and double-click on it.
f. In the pop-up, click 'New' and add a new row with the Tesseract path copied earlier in step b. Click OK in each dialog box to save the changes.
3. Create a Python virtual environment inside the folder with the script to process PDFs:
a. Open a terminal in this folder.
b. Install virtualenv using the command `pip install virtualenv`
c. Create a virtual environment using the command `virtualenv OpenAIEnv`. If you have multiple Python versions in your system, please use the -p option to specify the path to the Python 3.11 installation.
d. After the previous command has run successfully, enter the virtual environment using the command `source ./OpenAIEnv/bin/activate` (for Mac and Linux) or `OpenAIEnv\Scripts\activate` (for Windows).
**NOTE**: Please refer to the following [guide](https://www.geeksforgeeks.org/creating-python-virtual-environment-windows-linux/) for any issues in virtual environment creation.
e. Install the necessary Python libraries using the command `pip install -r requirements.txt`
**IMPORTANT**: Please ensure that you have downloaded the correct requirements.txt for this application and there is no other file of the same name in this folder.

### Running the application
1\. Please enter your OpenAI Key on line 301 

2\. Run the script using the command `streamlit run process_pdfs_and_compute_embeddings.py`. A browser window should open with the application.

### Using the application

The application walks the user through most of the steps required to process their PDFs. The UI contains detailed instructions and provides useful progress/ error messages as applicable. The section below is a brief description of the different options provided to the user:
1\. Specify a domain name: This is a text input which is used to store multiple sets of vector embeddings and document chunks for use with LLM Engines and switch between different sets on the fly\. A folder of this name is created to save the files at the end of the application\.
2\. \(Optional\) Upload Excel file with 'filename' and 'url': This Excel file is used to specify which URLs the LLM Engine should use when citing references in its answers\.
**NOTE**: If you do not have such an Excel file handy, the application will automatically generate one for you!
3\. If the Excel file was uploaded by the user\, an option will be presented to the user to upload files from local storage or to download the files\.
4\. If the 'Download' option is selected\, the application will attempt to download the files sequentially based on the links provided in the 'url' column\.
**NOTE**: The URLs must be publicly accessible! If the URL is private or sends no file back, the application will skip this file. Please check the terminal logs to find out if any files are being skipped.
5\. If the Excel file was not uploaded in step\-2 or the Excel file was uploaded and the 'Upload' option was selected thereafter:
a. The application provides a widget to upload all your PDFs. Multiple PDFs can be uploaded at once.
**IMPORTANT**: All PDFs should be uploaded at once - once a file/ set of files are uploaded, the application will ignore any files uploaded later. It is advised to keep all PDFs to be uploaded handy in a single folder.
b. If the Excel file was not uploaded the step-2, the application will provide the user with the option to specify their own custom URL header. E.g. For the URL 'https://dhhs.michigan.gov/Documents/BAM-117.pdf', the URL header would be https://dhhs.michigan.gov/Documents
If this option is not exercised by the user, then the application will proceed with a dummy URL.
6\. If documents were uploaded\, they are now checked against the names provided in the Excel file\.
**IMPORTANT**: This Excel file is treated as a source of truth. The filenames in the 'filename' column should match the names of the PDF files being uploaded. PDF files not present in the Excel will be skipped for processing.
7\. The application now parses all the documents\, chunks them page\-wise and then sends the chunked data to OpenAI's Ada Embedding model to create vector embeddings of the documents\.
8\. \(Optional\) After the emebddings are created\, the application offers the user to create emebddings of a Predefined Q&A set that the LLM Engine can refer to for answers prior to generating it's own answers\. This file must contain the following columns \- '\#': serial number of question\, 'Q': question\, 'A': corresponding answer\.
9\. Finally\, once all the embeddings are computed\, the application saves the document chunks and emebddings files inside the current directory under VectorDB/\{domain\_name\}\. 'domain\_name' is the name specified in step\-1\. This is designed for seamless integration with AskMe\.AI and Case Co\-Pilot LLM engines\.
10\. \(Optional\) The user also gets the option to download the processed files to their own separate location if they desire\.

### Contact

Please reach out the development team in case of any queries/ issues/ bugs:

1. Pratulya Shah (pbimalshah@deloitte.com)
2. Sumit Gupta (sumitgupta4@deloitte.com)
3. Shivam Chaudhary (shivamchaudhary3@deloitte.com)

We also appreciate your valuable feedback on the application, kindly contact development team to provide the same.
