---
sidebar_label: 'Custom Reactors'
sidebar_position: 2
slug: "java/custom-reactor"
description: "How to create reactors"
---

# Creating and Running a Custom Reactor


## Overview

A reactor is a business logic unit of **[Pixel](../../Java%20Development/java/pixels)** which performs the desired operation for the user. At its core, a reactor is just a Java class file. A reactor file is put inside a java folder within the App folder when custom backend code is required. It can directly interact with the AI Core server.

Example

```
package test;
import prerna.reactor.AbstractReactor;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.nounmeta.NounMetadata;

public class NewReactor extends AbstractReactor {

    @Override
    public NounMetadata execute() {
        System.out.println("Hello World!");
        return null;
    }
}
```

As shown above, it is a simple custom reactor which just print out **"Hello World"**. It inherits from parent class reactor called **Abstract reactor** which uses a common implementation method for subclass reactors for them to operate. We are using **execute** in this case for our custom reactor to implement.

Based on the requirements, logic (lines after execute) can be simple, as in the above example, or complex.

## Prerequisites

### Install Maven
**Maven** is a dependency management tool for Java projects. When creating custom reactors, you will borrow classes and methods from a **AI Core** source that AI Core maintains through its [Maven Repository](https://mvnrepository.com/artifact/org.semoss/semoss). 

Please make sure that you have already set up AI Core and installed Maven according to one of the guides below based on your chosen setup type:
* **If you use the public AI Core server or Dockerized AI Core (common)**: [Getting Started: Maven Setup](https://maven.apache.org/guides/getting-started/#:~:text=Sections%201%20What%20is%20Maven%3F%202%20How%20can,8%20What%20is%20a%20SNAPSHOT%20version%3F%20More%20items)
* **If you use a fully local installation***: [Download Required Software](../../Advanced%20Installation/Local%20BE%20Install%20Guide.md)

  _*Fully local installations are uncommon and typically only recommended for users who wish to directly modify the AI Core source code._

### Create your package directory structure
1. To get started, navigate to the `java` folder inside of your app project folder. If you have not created an app project folder yet, please review the project directory structure [here](/How%20To/App%20Creation%20Guides/React%20App%20In-Depth%20Guide#app-structure) first.
2. Create the following unique identifiers using **lowercase a-z characters only**:

   - **Group ID**: This identifier represents your organization/team or who you are.
   - **Artifact ID**: This represents your project name.

     For example, if you work for the Human Resources team and are developing a Staffing App, you could choose "humanresources" as your group ID and "staffingapp" as your artifact ID.

3. Download the starter Maven project from [here](../../../static/assets/CustomReactorGuide.zip)
4. Once it's downloaded, unzip the folder. Open the `CustomReactorGuide` folder. You should see a folder inside it called `yourartifactid`.
5. Move the `yourartifactid` folder into the `java` folder within your project directory.
6. Rename the `yourartifactid` folder to the Artifact ID you chose earlier (ex. "staffingapp"). Make sure you type the name in lowercase a-z only (no spaces, underscores, or dashes).
7. Navigate inside the Artifact ID folder that you just renamed. You should see a `src` folder and a file titled `pom.xml`.
8. Open up `pom.xml` in any text editor to replace any instances of `yourgroupid` and `yourartifactid`. These usually occur around lines 5-8:

  ```
   5 	 <groupId>yourgroupid</groupId>
   6	 <artifactId>yourartifactid</artifactId>
   7	 <version>0.1</version>
   8	 <name>yourartifactid</name>
   ```

   Replace `yourgroupid` on line 5 with the actual group ID you chose in the previous section.
   Replace `yourartifactid` on lines 6 and 8 with the actual artifact ID you chose in the previous section. **Save and close the file.**

   > **Note**
   > If you want to use your own `pom.xml` for your project instead of our starter template, simply add the following snippet to the `dependencies` block:
   
    ```
    <!-- https://mvnrepository.com/artifact/org.semoss/semoss -->
    <dependency>
       <groupId>org.semoss</groupId>
       <artifactId>semoss</artifactId>
       <version>4.2.2</version>
    </dependency>
    ```
 
9. Navigate into the `src/main/java` folder. You should see a folder titled `yourgroupid`
10. Rename the `yourgroupid` folder as the Group ID (ex. "humanresources"). Make sure you type the name in lowercase a-z only (no spaces, underscores, or dashes).
11. Navigate inside the Group ID folder that you just renamed, where you will see another folder named `yourartifactid`. Rename this folder according to the Artifact ID you chose earlier.
12. Return to the `src` folder and navigate into the `src/test/java` folder. Repeat steps 10-11 inside this folder as well.

### Recommended: Import your Project into your IDE

Most popular IDEs that support Java will also provide Maven extensions and easy workflows to import and build Maven projects. Please check out the following IDE-specific guides on how to **import an existing Maven project** into your preferred IDE.

- [VSCode](https://code.visualstudio.com/docs/java/java-build)
- [Eclipse](https://www.baeldung.com/maven-import-eclipse)
- [JetBrains/IntelliJ IDEA](https://www.jetbrains.com/help/idea/maven-support.html)

## CustomReactor Guide

> **Note**
> In this guide, we will walk through creating a reactor called `CustomReactor`. You can choose to name your reactor something else, but please keep in mind the following naming conventions:
>
> - The first letter of the name is capitalized.
> - The name of the class ends in `Reactor`. For example, `LLMReactor`, `MyFavoriteReactor`.
> - The pixel call that initiates the reactor is case-sensitive and is determined by what comes before "Reactor" in the name. For example, if you create a reactor called `TalkReactor`, then the corresponding pixel call is `Talk()`

### Start with a Reactor Template

1. In your preferred IDE, open up the `java/yourartifactid` folder as your project directory/workspace.
2. View the CustomReactor.java file located at `java/yourartifactid/src/main/java/yourgroupid/yourartifactid/reactor` in your IDE. The first line looks like:

   `package yourgroupid.yourartifactid.reactor;`

   Replace the value of `yourgroupid` and `yourartifactid` with the actual identifiers that you chose in the previous section.

3. View the CustomReactorTest.java file located at `java/yourartifactid/src/test/java/yourgroupid/yourartifactid` in your IDE. The first line looks like:

   `package yourgroupid.yourartifactid`

Replace the value of `yourgroupid` and `yourartifactid` with the actual identifiers that you chose in the previous section.

### Test your Maven Integration

1. If you are using an IDE with Maven integration **(recommended)**, use your IDE's Maven utilities to **clean** the Maven project, **update/validate** it, and **compile or package** it.
2. If not, then open up a command prompt/terminal and `cd` to your `java/yourartifactid` folder. Then, run the following commands and ensure that Maven outputs a **"BUILD SUCCESS"** message after each command.
   - `mvn clean`
   - `mvn validate`
   - `mvn package`
     
### Try out the CustomReactor

Return to the CustomReactor.java file. Note the lines:

```
    final static String NAME_INPUT_STRING = "name";

    public CustomReactor() {
        this.keysToGet = new String[] { NAME_INPUT_STRING };
    }
```

The **`keysToGet`** field is a list of inputs the reactor should accept. These input argument names are stored as a String array.
For the CustomReactor example, we have specified that the reactor should have one input field which is **"name"**.

Then, the next block of code in the `execute()` method provides the instructions for what the reactor should do.

```
    @Override
    public NounMetadata execute() {
        organizeKeys();
        String userName = this.keyValue.get(NAME_INPUT_STRING);
        String message = "Hello " + userName + "!";
        return new NounMetadata(message, PixelDataType.CONST_STRING);
    }
```

In this case, CustomReactor will return a String that says "Hello YOUR_USERNAME!", where YOUR_USERNAME represents a name that was received from the user inputs.

To confirm that the reactor actually works, open up the CustomReactorTest.java file located at `java/yourartifactid/src/test/java/yourgroupid/yourartifactid` in your preferred IDE. If you are working with an IDE that has **JUnit integration**, you can run the test and verify that the reactor output matches the expected string "Hello YOUR_USERNAME!"

## Coding Your Own Reactor

To create your own reactor, you need to define what user inputs the reactor needs and to specify these as the `keysToGet`.

```
public CustomReactor() {
       this.keysToGet = new String[] {"firstName", "lastName", "age"}
}
```

As shown in the above example, there are 3 input arguments indicating that the reactor expects to receive a "firstName", "lastName", and "age".

You can put any number of input arguments, and you can also specify whether these arguments should be **required** or **optional**.

These input values will be passed into the reactor through a pixel call, which is run in the AI Core terminal. When we enter inputs using a pixel call, it identifies which Java reactor to interact with using those inputs.

### How data is passed into a reactor

Each reactor has a NounStore Object, under the variable name store.

What is a **NounStore**?  
It is a collection of keys pointing to another object, called a **GenRowStruct**.

What is a **GenRowStruct**?  
It is a vector which stores inputs as **NounMetadata** objects.

What is a **NounMetadata**?  
It is a wrapper around any Object value, PixelDataType nounType.

The store will hold all the information that is passed into the reactor, but based on how the information is sent, it will store it in different locations. Upon reactor initialization, an empty GenRowStruct is created.

### Override execute() method

Just like creating any other reactor, we will override the `execute()` method from **AbstractReactor**. We want to end this method by returning the NounMetadata results from the reactor we called here, so it also gets returned to the user when this reactor is used. First run the User input 1 string, followed by User input 2 string and then the third User input and so on as shown below and it will execute the actions.

```
    @Override
    public NounMetadata execute() {
        organizeKeys();
        String firstName = this.keyValue.get("firstName");
        String lastName = this.keyValue.get("lastName");
        String age = this.keyValue.get("age");

        String greeting = "Hello, " + firstName + " " + lastName + "! You are " + age + " years old."

        return new NounMetadata(greeting, PixelDataType.CONST_STRING);

    }
```

Here, we override the `execute()` method to take in the **"firstName"**, **"lastName"**, and **"age"** keys passed in to return a greeting message. If I run the reactor call `CustomReactor("John", "Doe", "30")`, the reactor will return **"Hello, John Doe! You are 30 years old."**.

Put the Reactor file in inside Java folder within the App folder for App to be used.

### Extending What Your Reactor Can Do

In the above example, notice that the top of the reactor file has some import statements, like:

```
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.nounmeta.NounMetadata;
```

These import statements refer to classes that AI Core provides through its [Maven Repository](https://mvnrepository.com/artifact/org.semoss/semoss). There are many more AI Core classes, including (at least 987) reactors, that you can "borrow" by importing into your own code as shown above. Utilizing AI Core's classes can supercharge the range of different functions that your reactor can perform.

## Validating and Testing a Reactor

1. Now that a custom reactor has been coded/developed, the next step is to add the reactor to your java folder and test it using the **Editor**.

   For that, open the AI Core server and and click on **BI** in App Library as shown below.
   ![BI](../../../static/img/Creating%20a%20custom%20reactor/InsideBI.PNG)

   > _BI in App Library_

2. Next, under My Insights - click Create + New Project.
   ![ProjectButton](../../../static/img/Creating%20a%20custom%20reactor/AddNewProjectButton.PNG)

3. Mark your project as private and provide it with any name (Note: This is for your personal use). Select your project under the insight tab and copy the ID from the URL.
   ![ProjectPanel](../../../static/img/Creating%20a%20custom%20reactor/CreateNewProjectPanel.PNG)

4. Now on the left nav bar, click on **Add New Insight**. Then, go to the **CFG Terminal** (next to the Save button) and click **Editor** as Mode and then **Project** as your workspace. Please select your project that you created in step 3 from the dropdown. Enter your **version** -> **assets** folder. Create a java folder, a py folder, and a portals folder. Upload your reactors in the java folder. Upload any python files that your reactors use in the py file. Whenever you change your reactor or python file delete it from this directory and re-upload.
   ![CreateJavaFolder](../../../static/img/Creating%20a%20custom%20reactor/CreatingJavaFolder.PNG)

5. Run the following commands to test your reactors:

- `ReconnectServer(true)` - Run this if you ever get an analytic engine not available or if you made changes to your py file. Otherwise, you can ignore this step.
- `SetContext("Insert ID obtained from step 3")`
- `Compile("Insert ID obtained from step 3")`
  ![ConsoleCommandsSetProject](../../../static/img/Creating%20a%20custom%20reactor/ConsoleCommandstoSetProjectContextandTestReactor.PNG)

6. Now, run your reactor. If the name of your Java file is "TestReactor.java", then to validate your reactor in the terminal, you can now run Test().

- Note: Any changes to any file need to be recompiled using the Compile command.

7. In your front-end code. Whenever your load your app, make sure to run these pixel calls before any other reactors:

- `ReconnectServer(true)` - Run this if you ever get an analytic engine not available or if you made changes to your py file. Otherwise, you can ignore this step.
- `SetContext("Insert ID obtained from step 3")`
- `Compile("Insert ID obtained from step 3")`
- Note: Remove those three commands when you deploy your app to CFG. These commands are setting the context to your temporary project. However, when you deploy an app it's context is set to itself so there is no need to use another source. Remember that any changes to your reactors or Py file need to be uploaded to the project through step 4.

## Calling a Reactor using Pixel

Now after creating a reactor the next step is to Test whether reactor is working as required. For that, open the AI Core server and and click on **Terminal** in App Library as shown below.

![Terminal](../../../static/img/Creating%20a%20custom%20reactor/Terminal.PNG)

> _Terminal in App Library_

It will take you into the AI Core terminal where we can use pixel calls to run the reactor. Below you can see how the terminal looks.

![AI Core Terminal](../../../static/img/Creating%20a%20custom%20reactor/Inside%20Terminal.PNG)

> AI Core Terminal_

Enter the reactor you want to run and Add user inputs of that reactor within the bracket(). See the illustration below.

![Pixel Call](../../../static/img/Creating%20a%20custom%20reactor/Enter%20pixel%20call.PNG)

> _Calling a Reactor using Pixel Call_

Now enter and run the reactor and if it does not return any error, reactor is successfully tested.

## Using a reactor in Front-end code

For using reactor in App code, install the SDK using a package manager:
`npm install @semoss/sdk`

Now, to import the `Insight` from the SDK and create a new instance of it, add below line in App code.

```
import {Insight} from '@semoss/sdk';
const insight = new Insight();
```

Insights are temporal workspaces that allow end users to script and interact with a model, storage engine, or database.

Once the application is mounted, initialize the insight and load your data App into the insight by putting below line in App code;

```
await insight.initialize();
```

After initialization, add below line to run the pixel call.

```
await insight.actions.pixel call
```

Pixel call could be **login-logout, askModel, queryDatabase,run, upload, download etc.**

With this line in the App code, pixel call will run the reactor in the App.

Examples are shown below:

### Query a LLM and return a result

```
const ask = (question) => {
    const { output } = await insight.actions.askModel(MODEL_ID, question);

    // log the output
    console.log(output);
};
```

### Run a database query

```
const getMovies = () => {
    const { output } = await insight.actions.queryDatabase(
        DATABASE_ID,
        'select * from movie',
    );

    // log the output
    console.log(output);
};
```
