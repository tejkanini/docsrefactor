# Formatting and Style Guidelines for Documentation Team
> **Warning**
> **Update**: As of October 31, 2023, the formatting for CortexAI for Government is now **SEMOSS**. Please replace any existing instances of "SEMOSS" and associated permutations (ex. "CfG") with the official "**SEMOSS**" form. Please also note that the shortened "CFG" form is not valid. See below for the list of accepted formats.

## Capitalization Conventions
> **Important**
> Please define all acronyms / abbreviations before they are used.

| Name                          | Valid Formats                        | Invalid Formats               |
|-------------------------------|--------------------------------------|-------------------------------|
| CortexAI for Government       | CortexAI for Government, SEMOSS      | CFG, SEMOSS, SEMOSS, SEMOSS   |
| Semantic Open Source Software | SEMOSS                               | Semoss, semoss                |
| SEMOSS SDK                    | SEMOSS SDK                           | Semoss sdk, semoss sdk        |
| Generative AI                 | Generative AI, Gen AI                | genAI, gen AI, gen ai         |
| Java                          | Java                                 | java                          |
| Python                        | Python                               | python                        |
| Hypertext Markup Language     | HTML                                 | html                          |
| Cascading Style Sheets        | CSS                                  | css                           |
| JavaScript                    | JavaScript, JS                       | javascript, Javascript, js    |
| JavaScript Object Notation    | JSON                                 | json                          |
| Visual Studio Code            | Visual Studio Code, VSCode           | VsCode, vscode                |

## Style

To view how any of the formatting is created in this doc, you can **view the raw file** or view the file in the **Edit** or **Code** modes.

### Callouts
Use the **"Note"**, **"Warning"**, and **"Important"** markdown headers to flag important information to the reader.
> **Note**
> Mention useful **notes** here that may interest the user but is tangential to the enclosing body text.
 
> **Warning**
> Discuss any crucial issues that the user should be cautious about using a **Warning** header.

> **Important**
> Flag key information for the user to review with the **Important** header.

> **Hints or Additional Info:**
> For additional notes that are tangential to the flow of the enclosing text, use a block quote.

<details>
 <summary>Click here for a hidden dropdown</summary>
 You can also hide tangential information in a dropdown section like this. 
</details>

### File Paths

Paths to files and directories should always be formatted as **fixed-width code** like so: `C:\Users\YOUR_USERNAME\Desktop`

Default to using **Windows** file path syntax unless specifically discussing setup steps for a Mac/OSX machine.

### Code Snippets

Please format all code or shell commands as **fixed-width code**, like so: `here is some code`

If there are parameters in the code that must be replaced by the user's own input, please **`CAPITALIZE`** and **`USE_UNDERSCORES`** on the parameter to be replaced, and then follow up by indicating to the user that their input is required. Here is an example:

> Run `cd C:\Users\YOUR_USERNAME\Desktop`, where `YOUR_USERNAME` refers to your actual laptop username.

Please format any **multi-line commands** or **code snippets** as a block of code, ex:
```
this is a sample
to show some multi-line code
also a haiku
```

### Links
If you are linking to a document within the `SEMOSS/documentation` repository, please use the **relative link**, not the full HTTP URL path. 

For example, to create a link to the [README](README.md), use the following markdown syntax:
`[README Link Text](README.md)`

DO NOT use: `[README Link Text](https://github.com/Deloitte-Default/cfgai-docs/blob/master/README.md)`

The same applies to linking to a **section** of a document. For example, to link to the [table of contents](README.md#table-of-contents) in the README, use this syntax:

`[Table of Contents Link Text](README.md#table-of-contents)`

NOT this:

`[Table of Contents Link Text](https://github.com/Deloitte-Default/cfgai-docs/blob/master/README.md#table-of-contents)`

> **Warning**
> Be careful when changing section headings or document names, as it could cause links to break. For example, if you rename **README.md** to **README2.md**, then you must replace every link that references **README.md** across all files in the repository. Similarly, if you rename the **"Table of Contents"** heading in the README.md to **"Preview"**, then you must go through all the files in the repository to find any links that previously referenced the README's "Table of Contents" section and update it to now reference "Preview."

### Images

Caption images with a **note in italics** and add **alt-text** when embedding images, like this (hover over the image to view the alt-text pop-up):
  
![Alt text goes here](images/FELocalInstall/finalPage.PNG)
> _This is my caption_

As mentioned in the [Links](#links) section above, if your image is stored within the `SEMOSS/documentation` repository, then please embed it using the **relative path** of the image, not its full GitHub HTTP URL.
