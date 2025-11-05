# Data Apps Guide  

## Table of Contents:  

1. [Overview](#1-overview) 
2. [Initialize a new CFG Workshop Project](#2-initialize-a-new-CFG-workshop-project) 
3. [Backend Development](#3-backend-development) 
4. [Frontend Development](#4-frontend-development) 
5. [Deploy Portal App](#5-deploy-portal-app) 
6. [Run Deployed App](#6-run-deployed-portal-app)
7. [Using the SEMOSS SDK](#7-using-the-semoss-sdk)

___

## 1. Overview  
  
### Why Open Source/ local Large Language Models (LLMS)?
1. **Flexibility**  
 The ability to go from one model to another if a client has a hosted LLM they want to use. This demonstrates the art of the possible to a client.  

 2. **Data Sensitivity**  
 The ability to control where the data is going and what is stored. 

3. **Pricing**  
 Pricing may be cheaper now, but OpenAI can increase their pricing.

4. **Reinforcement**  
 Local models allow us to apply RLHF to improve responses.  

5. **Shared Services Model**  
 Eventual hosted set of models that can be shared between Deloitte-hosted SaaS solutions. 

### Data 

* **No PII/PHI, Client Data, and Sensitive Data**
* Databases - SQL Server or Snowflake 
* Unstructured Data -PDFs, JSONs can sit in Project folders and be backed into a Google Bucket or S3
* External DB connection notes: 
    * Not generally used or allowed, but it can be on a case-by-case basis. Snowflake is an exception and is fine to use 
 
___

## 2. Initialize a new CFG Workshop Project   

1. Through the CFG Workshop UI, create a new project and take note of the project ID.
![Create New Project](images/dataAppsImages/Picture1.png)
    * Project ID can be found by accessing the newly created project url.
    ![Project ID](images/dataAppsImages/projectid.png)
2. Download the project and work with it locally.
    * Click Manage and then export.
    ![Manage Project](images/dataAppsImages/manage_project2.png)
    ![Export](images/dataAppsImages/export.png)  

3. Unzip the file to a temporary location within your file explorer.  
    * Within the unzipped project folder, you will see a project folder and a .smss file. The .smss file contains configuration details used by the underlying platform to hold different pieces of information and add different properties. 
4. Open the .smss file with a text editor and add the following lines:
    ```
    OpenAI key 
    ```
    * This will remain blank for now    
      
    ```
    public_home_enable true
    ```
    * This will enable auto publish of the app upon server startup  

5. Within the project folder, navigate to the assets folder following the file path: project/app_root/version/assets. 
    * Note: The project folder is git enabled so you may pull code.  
6. Create a folder called "portals" and "java." 
    * The "portals" folder will house your front end code. Within this folder, create a simple index.html file. 
    * The "java" folder contains reactors, which are similar to commands for different scripts. 
___

## 3. Backend Development

1. Navigate to the "assets/java" folder.
2. Create a reactor specific file with .java extension. Please maintain the file name format as &lt;reactorName&gt;Reactor.java.
3. Let's take Application-Framework as a base for referring how to create backend reactors.
4. Create Reactor is specifically used for inserting data into a database. 
    * To create a reactor, we should follow file naming convention as &lt;reactorName&gt;Reactor.java.
    * This reactor should extend **prerna.sablecc2.reactor.AbstractReactor** as shown in the example below:  

        ```java
        public class CreateNoteReactor extends AbstractReactor
        ```  
    * We have the **execute()** method, which contains all the business logic required for a reactor.
    * We can get the input parameters from the **keyValue** object.  
        ```java
         public CreateNoteReactor() {
            this.keysToGet = new String[] {"description"};
        }
        ```  
        We must validate the input parameter and throw necessary errors, if required.

        ```java
        String description = this.keyValue.get(this.keysToGet[0]);
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Must provide a valid description");
        }

        ```
    * Inside **execute()**, we can create a database connection using the example below: 
        ```java
        RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(<<ENGINE_ID>>);
        ```

    * Using the above database connection, we can run any required SQL statement as shown in below image and we should return appropriate message using **NounMetaData**.
        * **&lt;&lt;insertNoteQuery&gt;&gt;** will indicate the SQL query to run.  

        ```java
        // perform the insert
        PreparedStatement ps = null;
        try {
            ps = database.getPreparedStatement(<<insertNoteQuery>>);
            int psIndex = 1;
            ps.setString(<<psIndex++>>, <<userId>>);
            ps.setString(<<psIndex++>>, <<description>>);
            ps.setBoolean(<<psIndex>>, <<false>>);
            ps.addBatch();
            ps.executeBatch();
            database.getConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() == null || e.getMessage().isEmpty()) {
                throw new SemossPixelException(
                    "Error occurred trying to submit the note. Detailed message = " + e.getMessage(), false
                );
            } else {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        } finally {
            ConnectionUtils.closePreparedStatement(ps);
        }

        NounMetadata noun = new NounMetadata("MESSAGE" + <<description>>, PixelDataType.CONST_STRING);
        return noun;
        ```
    * By using the above method, we can create multiple required reactors, recompile, and deploy them by following the steps below.  

CFG Workshop should automatically compile the backend reactors into a new folder called "classes." You can manually recompile the classes by running the pixel statement below:  

     ReloadInsightClasses("<PROJECT ID>")   

You can view compilation errors and warnings in the compiler.out file of "classes" folder.  


___

## 4. Frontend Development
1. Create a front-end project in any of the following frontend libraries:
    * Supported: React, AngularJS, plain HTML/JS (non-exhaustive)
    * Not supported: StreamLit  
2. Define the required environment/constants variables in .env file of project. Below are the required variables:  

    ```js
    MODULE=<<MODULE>>
    ENDPOINT=<<ENDPOINT>>
    DATABASE=<<DATABASE_ID>>
    PROJECT=<<PROJECT_ID>>
    ```
3. In the backend development section, we have seen an example of creating *CreateNoteReactor*, which accepts ***description*** as a parameter. Now let's see how we can create a run method to run the pixel on the backend. 
4. We must create a *run* and *runQuery* method, which will call the backend API to run the pixel on the given insight. 

    ```js 
    async run(insightID: string, pixel: string) {
        // build the expression 
        let postData = '';

        postData += 'expression=' + encodeURIComponent(pixel);
        if (insightID) {
            postData += '&insightId=' + encodeURIComponent(insightID);
        }
        const response = await axios
            .post<{
                insightID: string;
                pixelReturn: {
                    isMeta: boolean;
                    operationType: string[];
                    output: unknown;
                    pixelExpression: string;
                    pixelId: string;
                }[];
            }>(`${MODULE}/api/engine/runPixel`, postData, {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded',
                },
            })
            .catch((error) => {
                // throw the message 
                throw Error(error.response.data.errorMessage);
            });

        // there was no response, that is an error
        if (!response) {
            throw Error('No Pixel Response');
        }
        return response.data;
    }

    /**
     * Run a pixel off of the query insight 
     * 
     * @param pixel - pixel to execute
     */
    async runQuery(pixel: string) {
        const {configStore} = this._root;

        return this.run(configStore.store.insightID,pixel);
    }
    ```

    In the above code snippet, we are creating ***postData*** containing ***insightId*** and ***pixelExpression***, which we will pass as an input to the backend runPixel API. 
5. After creating the run and runQuery method, we will call this by constructing the pixel query. 
    ```js 
    .runQuery(`CreateNote ( description = [ "${toDo}" ] )`)
    .then((response) => {
        const { operationType } = response.pixelReturn[0];
        //track the errors
        if (operationType.indexOf('ERROR') > -1) {
            setMessage('Error submitting note');
        } else {
            setMessage('Note successfully archived');
        }
    });
    ``` 
    We are passing pixel query to runQuery method, which contains which reactor to call and the required parameters. We must pass all the parameters in ***keysToGet*** property inside the reactor constructor on the backend. 
6. By using the above structure, we can call the run method to run any required pixel on a given insight. 
7. Build your project and rename it to your application name. 
    
___

## 5. Deploy Portal App  

1. Copy and paste all necessary files into the assets folder as shown in the example below: 
![Assets Folder](images/dataAppsImages/assetsfolder.png)
2. Compress the assets folder into a zip file so that it can be easily brought back into the server. 

3. Start CFG Workshop and click on "Add New Insight" button.  
![Add New Insight](images/dataAppsImages/Picture2.png)  

4. Open the terminal by clicking "Open Terminal" icon in the top right corner.  
![Terminal](images/dataAppsImages/Picture3.png)  
5. Select Editor mode and click on Project under Select Workspace. 
![Project](images/dataAppsImages/Picture4.png)  
6. Select your project from the dropdown and obtain your project ID. 
7. In the terminal, run the following pixel command: 
    ```
    SetContext("<project id>")
    ```
8. Switch to the Shell in the terminal and navigate to the assets folder. 
![Shell](images/dataAppsImages/shell.png)
9. Unzip your assets file and remove the existing asset folder. 
10. Switch back to pixel and run the following commands: 
    ```
    ReloadInsightClasses("<project id>");
    PushProjectFolderToCloud("<project id>");
    PublishProject("<project id>");
    ```
11.  After running the above pixels successfully, the project will be deployed on the URL output following the commands (Note: this may take a few seconds to load the application on the URL for the first time).

___

## 6. Run deployed Portal App 
Modify the below URL according to your project_id and folder name: 

**&lt;CFG WORKSHOP API>/public_home/<PROJECT_NAME>/portal/&gt;**

## 7. Using the SEMOSS SDK

1. Once your application has been added to the SEMOSS mono-repo, then that is when you can integrate your app by using the SEMOSS SDK to deploy your app as an insight within SEMOSS. 

SEMOSS SDK is a library which can be added to your application through your preferred package manager. This guide will briefly talk about how to integrate it based on having an React app, and also show the code necessary for non-React based applications (for example applications using Vanilla HTML/CSS). 

React

2. If your application is using a React based framework, then implementation of the SEMOSS SDK is relatively straightforward. It is implemented similarly to the Semoss component library, semoss-ui, in fact both are internal packages that are available to all semoss-apps and packages to use. 

3. If you have not implemented semoss-ui, then you can implement the SDK by going to your application's package.json. This section should look similar to this:

semoss/apps/your_app/client/package.json

    ```js
        "private": true,
        "scripts": {
            "build": "webpack --mode=production --node-env=production",
            "dev": "webpack serve",
            "lint": "eslint . --ext .js,.jsx,.ts,.tsx",
            "lint:fix": "eslint . --ext .js,.jsx,.ts,.tsx --fix",
            "format": "prettier . --check --plugin-search-dir=.",
            "format:fix": "prettier . --write --plugin-search-dir=.",
            "fix": "run-s format:fix lint:fix",
            "type-check": "tsc --pretty --noEmit"
        },
        "dependencies": {
            "@semoss/ui": "workspace:*",
            "@semoss/sdk": "workspace:*",
        },
        "devDependencies": {},
    ```

As you can see we have added both the UI and the SDK as workspaces within the projects dependencies. This will build and index the SDK library within your application. 

4. The main purpose of the SDK is to give you access to the useInsight() hook. This creates an insight context for your application. ALlowing you to build your app as a functional workspace within semoss, which will also make it available in the app-library. 

5. Once you have access to the SDK, then you will want to wrap your Application within the SDK's InsightProvider:

apps/your_app/client/src/App.tsx
    ```js 
        export const App = () => {
            return (
                <ThemeProvider reset={true}>
                    <InsightProvider appId={APP}>
                        <Router />
                    </InsightProvider>
                </ThemeProvider>
            );
        };
    ```

With your appId, the Insight Provider is able to pull all the information from your app using a Pixel call. You are then able to grab these values using the useInsight() hook within your authentication actions to allow integration into the SEMOSS app library. 

Vanila JavaScript

6. If your application does not use a React frontend framework, then instead of using the above hook the best way of integrating is to use the Insight Class within your application. This works similar to the InsightProvider, in that you wrap the class around your current application. 



