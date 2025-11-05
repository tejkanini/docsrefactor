# Pixel Calls

## What is a Pixel Call
Pixel is a domain specific language (DSL) specific to SEMOSS that is used as the payload for all the operations that can be performed on an insight. Every Pixel has a java class to handle the business logic on the backend, we call this java class a "Reactor".

## How to write a Pixel Call
A Pixel call has some important components:
1. PixelCommand the name of the pixel logic to execute
2. Inputs
    1. Inputs may use keys to define them
        ```
        AddColumn(newCol=["ColA"], dataType=["VARCHAR(50)"]);
        ```
    2. If PixelCommands do not use keys, then the order of the inputs is important!
       ```
        POWER(2, 3);
        POWER(3, 2);
        ```
3.  Some Pixel commands use pipes (“|”). A pipe is used to indicate that you would like to chain a Pixel command with a previous one. Essentially, the output of one reactor is taken and put into the next reactor so that the Pixel commands build off of each other.

4.  Finally, a semi-colon (“;”)! A semicolon is a terminator and will be found at the end of your chain of commands. A semicolon indicates that you want to create a sink. It is best to create a sink when you are at a logical endpoint, meaning you do not need to use the output of your command for another reactor. For example, a logical endpoint is when you want to push your data to the frontend of SEMOSS so that it can be viewed.

<!-- create a sink, sorry if I'm misinterpreting. I wouldn't over explain this just say a (;) means it is the end of the chain of commands-->

## List of Useful Pixel Calls 
There are many pixels calls/commands available that allows us to perform various kinds of tasks. To get a comprehensive list of all available Pixel commands, you can achieve this by simply typing "Help()" into the console. 
![Help](images/PixelCall/Help1.PNG)

### Common Pixel Calls
#### Engines

<!-- Overall Sample Pixels would be great, the create ones we don't necessarily need for everyone just the one i provided on ln 38 should be good -->

* **CreateModelEngine():** This allows you to setup and configure a new LLM into your backend. You can specify the type of model and other details using 2 input parameters.
    - *Input 0: model* - Fill in the name/ engine ID of the model you want to use within ("")
    - *Input 1:* modelDetails* - Provide the map or collection of data that speciifes details to establish connection with the model engine within (""), each element separated by a coma(,).

    <!-- Sample: CreateModelEngine(model=["NEW WIZARD"], modelDetails=[{}])-->

* **CreateStorageEngine():** This allows you to setup and configure a new storage engine into your backend. You can specify the type of storage and other details using 2 input parameters.
    - *Input 0: model* - Fill in the name/ engine ID of the storage you want to use within ("")
    - *Input 1: modelDetails* - Provide the map or collection of data that speciifes details to access and interact with the storage engine within (""), each element separated by a coma(,).

* **CreateVectorDatabaseEngine():** This allows you to setup and configure a new vector database engine into your backend. You can specify the type of storage and other details using 2 input parameters.
    - *Input 0: database* - Fill in the name/ engine ID of the database you want to use within ("")
    - *Input 1: conDetails* - Provide the map or collection of data that speciifes details to establish connection with the database within (""), each element separated by a coma(,).
    - *Input 2: filePaths* - Any additional around file paths that may be required.

* **Engine (get for your storage, etc)**
    - *Input 0:*
    <!-- Is this an actual Reactor at the moment, I know Database() is which I'd assume Engine would be the same way but not built out yet -->

#### GenAI
* **CreateEmbeddingsFromDocuments()** This allows you to generate embeddings from data or documents that are already stored in your backend. 
    - *Input 0: engine* - Fill in the name/ engine ID that you want to use for embedding data within ("").
    - *Input 1: filePaths* - Provide the file path or data sources from which you want to create embeddings within (""). 
    - *Input 2: paramValues* - Depending on the engine that you're using, you might have to specify additional parameters or options within ("") separating each parameter/option by a coma(,).

* **LLM():** This allows you to run query on any chosen LLM and interact with the underlying workspace**
    - *Input 0: engine* - Fill in the name/ engine ID of the model you want to run your queries on within ("").
    - *Input 1: command* - Provide the query or command than you want to execute within ("").
    - *Input 2: context* - Specify the directory for running the queries within("").
    - *Input 3: paramValues* - Define parameter names and their corresponding values within ("") separating each parameter by a coma(,).

<!-- For the general GEN AI pixel calls a sample pixel call i think would be important for each one of these.  Since we are transitioning to these guys with the -->

#### Accessing Data

* **Database():** This allows you to specify the database that you want to use or identify it as a different variable, example X = Database ().
    - *Input 0: engine* - Fill in the name/ engine ID of the database you want to refer.

<!-- Currently I'm seeing this being done as: Database(database=[""]) -->

* **Frame():** This allows you to specify and define a frame/ container for organizing and arranging your data into tables, charts, and other visual formats.
    - *Input 0: frame* - Provide the name/ ID of the frame that you want to work with.

* **Insert():**
    - *Input 0: frame* - 

    <!-- Inputs are different -->

#### Running scripts
* **Py():** This allows you to
    - *Input 0:*

* **Command():** This allows you to execute shell commands within your backend.
    - *Input 0: command* - Provide the command that you want to run. Commands supported are
        - `cd`
        - `dir`
        - `ls`
        - `copy`
        - `cp`
        - `mv`
        - `move`
        - `del <specific file>`
        - `rm <specific file>`
        - `git`

#### API Calls
* PostRequest()
        - **Usage:**
        - **Inputs:**
* GetRequest()
        - **Usage:**
        - **Inputs:**
* SendEmail()
        - **Usage:**
        - **Inputs:**
  
### How to use Help Function
To understand the pixel calls/commands and their input options, you can run the "--help" function. 
1. Type the name of the command you're interested in, followed by two hyphens and the word "help." 
    - For example, to know about the input options for "CreateModelEngine" command, you'd enter "CreateModelEngine --help" in the console.
![Help1](images/PixelCall/help2.png)

2. You can see all the "Inputs" block in the console output.

<!-- ^^ Let me know if you need a hand accessing this for the other pixel calls you do not know inputs to ^^ -->

