# Send a Simple Email
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [SimpleEmailReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/email/SimpleEmailReactor.java) to follow along with this tutorial.**<br>
In this tutorial, we'll build and send a simple email using an HTML file. 

To send an email using SEMOSS, we can take advantage of a few built in email utility classes, namely the EmailUtility.java class and some form of a EmailBodyUtility.java class. Using these classes, we can construct the email by setting the subject, recipients, generating the email body using an html file, inserting in dynamic strings, and more.
___
### User input
> Pixel input is covered in [this tutorial](https://repo.semoss.org/semoss-training/backend/-/blob/master/tutorials/PixelInput.md)

The SimpleEmailReactor has two input parameters: **email** and **name**. The email that is inputted will be the recipient of the message and the name will be used within the message.

```java
// grab the user input and define variables
organizeKeys();
String email = this.keyValue.get(this.keysToGet[0]);
String name = this.keyValue.get(this.keysToGet[1]);
```

## Get Project Properties
In order to configure the email, we must obtain project properties. These properties will be used to determine server settings, social.properties file locations, etc. 
First, define the project ID, then use the *Utility* class to get the project properties:
```java
// grab project properties using helper methods
// projectProps will be used to determine server settings, property file locations, etc. 
String movieProjectId = "39fb01b3-c2d2-42ef-b95d-5a4d7f02a352";
ProjectProperties projectProps = Utility.getProject(movieProjectId).getProjectProperties();
```
## Defining Dynamic Strings
The **generateEmailBodyFromTemplate** method from the **TutorialEmailBodyUtility** class takes in a **Map<String, String>**, which we name **replaceStrings**, that will be used to insert string values into the HTML file that will be sent. This map will replace any string in the HTML file that matches any of it's keys with the value associated with that key. In other words: for the map, the string as it appears in the HTML file should be the key and the value is the string we want swapped in. A good practice is to surround these strings with the $ symbol before and after the text.	
In this example, the **\$NAME\$** string at the top of our HTML file will be replaced with the value of the **name** variable, which is determined by user input.
```java
Map<String, String> replaceStrings = projectProps.getEmailStaticProps();
replaceStrings.put(TutorialEmailBodyUtility.NAME, name);
```
## Building The Email
Now we can build the email by defining the subject, recipients, and email body. There are other options when building an email, but we'll be keeping it simple here. 
```java
String subject = "Simple Email Tutorial Subject";
// generate email body using email body utility, strings values to be inserted into the email are passed in here
// we need to call .getTemplateContent() on the enum that refers to our desired HTML file
String emailBody = TutorialEmailBodyUtility.generateEmailBodyFromTemplate(TutorialEmailTemplate.SIMPLE_EMAIL_HTML.getTemplateContent(), null, replaceStrings);
String[] recipients = {email};
String[] ccRecipients = {"throwaway@example.com"};
String[] bccRecepients = {"throwaway@example.com"};
```
#### social.properties file
The *social.properties* file is a file that, among other things, contains static values to be used in emails. These values often include things like web page links.  These values are then inserted into the email wherever their key matches a string in the file when the body is constructed. In this case, we insert a link to the Deloitte homepage by  utilizing the *social.properties* file.

#### Plain Text Email
With the *EmailUtility* class, we have the option of sending an HTML email(which this tutorial is demonstrating) and a plain text email. In order to send a plain text email, instead of setting our *emailBody* variable to a generated template from an HTML file, you can instead define it with a string.
```java
String emailBody = "This is a simple text email body. Insert content here!";
```
Upon sending the email, be sure to set the **isHTML** boolean argument is set to false.
## Sending The Email
Finally, we call the **sendEmail** method of the **@EmailUtility** class by passing in all of the required arguments that we defined prior. Since we are using an HTML file as our email, be sure that the **isHTML** boolean argument is set to true.
```java
Boolean sendEmailResult = false;
if (EmailUtility.sendEmail(projectProps.getEmailSession(), recipients, ccRecipients, bccRecepients,
	projectProps.getSmtpSender(), subject, emailBody, true, null)) {
		sendEmailResult = true;
}
NounMetadata noun = new NounMetadata(sendEmailResult, PixelDataType.BOOLEAN);
return noun;
}
```
___
And that's all you need to know in order to build and send a simple email using SEMOSS utility classes. Feel free to play around with these classes
