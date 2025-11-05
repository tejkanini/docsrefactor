---
sidebar_label: 'React App In-Depth Guide'
sidebar_position: 4
---

# Creating a AI Core GenAI App Using React

## Overview
This documentation focuses on the front end aspects of building a GenAI app. React is a Javascript-based User Interface (UI) framework. It improves reusability of UI elements to keep code readable and DRY ("Don't Repeat Yourself"). You can leverage React to develop a hosted front end with the potential to have some custom backend functionality that is deployed on the AI Core Server.

The languages used in this app will be Typescript/Javascript, and HTML in building your app. Typescript is an extension of Javascript that improves code quality and allows for a better developer experience.

## Prerequisites
### Node.js
You can install Node on your machine by downloading the appropriate installer for your device directly from the Node.js website. Version [v18.16.0](https://nodejs.org/download/release/v18.16.0/) is suggested for our projects (for most Windows machines, you should download `node-v18.16.0-x64.msi`). 

### Code Editor
You'll need to have your code editor of choice downloaded on your machine. We suggest [Visual Studio Code](https://code.visualstudio.com/). 
<!--Should we mention something about installing VSCode extensions for typescript/javascript?-->

## Examples
You can see app examples in the [AI Core Apps](https://github.com/Deloitte-Default/cfgai-apps) repo. Feel free to download an existing app and use it as scaffold for building your own! Existing work can be used to experiment and learn.
> **Note**
> This is a protected GitHub, if you do not have access to it, or are not currently signed in to your GitHub account you will get a 404 Error. Ask an admin for access.
<!--Should we link to the public CFG demo here so that first time users can click around and explore the apps in the live site? For a user encountering this guide for the first time, they probably won't know how to interact with the apps in the CFGai-apps repo. It might be useful to copy the "Adding your app to the AI Core catalog" section of this guide over to the CFGai-apps repo's README.md so that users can be guided on what to do after they've pulled the repo (ex. zipping the files and adding the app within CFG)-->
<!--Since this is a React app use case, do all the examples in the CFGai-apps repo also use React? I'm not sure how important this is but it might be helpful for users following this guide to base their app off of another React app in the CFGai-apps repo-->

## App Structure
Below is the typical structure of a React app to be uploaded onto the AI Core platform. The `/java` and `/py` directories hold back-end work. This document will focus on the `client` and `portals` folders.
<pre>
  <code>
    ├── <a href="#client">client</a>
    │   ├── <a href="#clientsrc">src</a>
    │   │   ├── <a href="#clientsrcassets">assets</a>
    │   │   ├── <a href="#clientsrccomponents">components</a>
    │   │   ├── <a href="#clientsrcpages">pages</a>
    │   │   ├── <a href="#clientsrcutilities">utilities</a>
    │   │   ├── <a href="#clientsrcapptsx">App.tsx</a>
    │   │   ├── <a href="#clientsrcglobaldts">global.d.ts</a>
    │   │   ├── <a href="#clientsrcmaintsx">main.tsx</a>
    │   │   ├── <a href="#clientsrctemplatehtml">template.html</a>
    │   ├── <a href="#clientenv--clientenvlocal">.env/.env.local</a>
    │   ├── <a href="#clientpackagejson">package.json</a>
    │   ├── <a href="#clienttsconfigjson">tsconfig.json</a>
    │   ├── <a href="#clientwebpackconfigjs">webpack.config.js</a>
    ├── <a href="#portals">portals</a>
    ├── java
    ├── py
  </code>
</pre>

### `/client`

#### `/client/.env` / `/client/.env.local`
See the Environment Variables section of [Connecting to the CFG App](../Establish%20Connection%20to%20CFG%20Portal/Connecting%20to%20CFG%20AI.md)

#### `/client/package.json`
NPM/PNPM stands for Node Package Manager and is included with Node. (PNPM is a newer, faster version of NPM but both can be used interchangably. If you find you don't have the `pnpm` command, you can install it with the following command: `npm install -g pnpm`.) 

NPM is how you can install [Node packages](https://www.npmjs.com/) that will help you on your app building journey. If you are using an existing project as a scaffold, you can run `pnpm install` **from the `client` directory** to download and install all the packages that are already in your package.json file. Otherwise, manually install required and suggested packages.

Below is a guide to required and suggested packages, but there are thousands out there that you can explore and use. Just make sure you choose ones that are reputable and actively managed!

##### Required Packages

| Package    | Installation Command |
| -------- | ------- |
| [react](https://www.npmjs.com/package/react)  | `pnpm i react` |
| [react-dom](https://www.npmjs.com/package/react-dom) | `pnpm i react-dom` |
| [webpack](https://www.npmjs.com/package/webpack)    | `pnpm i webpack` |
| [webpack-cli](https://www.npmjs.com/package/webpack-cli) | `pnpm i webpack` |
| [html-webpack-plugin](https://www.npmjs.com/package/html-webpack-plugin) | `pnpm i html-webpack-plugin` |
| [webpack-dev-server](https://www.npmjs.com/package/webpack-dev-server) | `pnpm i webpack-dev-server` |
| [dotenv](https://www.npmjs.com/package/dotenv) | `pnpm i --save-dev dotenv` |
| [SEMOSS SDK](https://www.npmjs.com/package/@semoss/sdk-react) | `pnpm install @semoss/sdk-react/npm install @semoss/sdk` |

##### Helpful Packages

| Package    | Installation Command |
| -------- | ------- |
| [Material UI](https://mui.com/material-ui/getting-started/installation/)  | `pnpm i @mui/material @emotion/react @emotion/styled` |
| [react-router-dom](https://www.npmjs.com/package/react-router-dom) | `pnpm i react-router-dom` |

#### `/client/tsconfig.json`
This is a configuration file that indicates that the directory is the root of a TypeScript project. It specifies the root files and the compiler options required to compile the project. Read more on it [here](https://www.typescriptlang.org/docs/handbook/tsconfig-json.html).

#### `/client/webpack.config.js`
This is a configuration file that tells Webpack how to bundle your Javascript files so they can be displayed in a browser. Read more on it [here](https://webpack.js.org/configuration/).

#### `/client/src`
The `src` directory holds all the source files that make up the front end of your app.

##### `/client/src/assets`
This directory stores all your web assets, like logos, photos, and graphics.

##### `/client/src/components`
React is a component-driven framework. You can read more about component-driven UI development [here](https://www.componentdriven.org/#why). You can think of components like elements of a larger picture or pieces of a puzzle. This directory will contain all the components you develop to be used in your app pages, and you can organize them into sub-directories as you see fit.

Note that in order to use the hooks provided by the SEMOSS SDK, the component must be a functional component. Here's a helpful [article](https://www.geeksforgeeks.org/differences-between-functional-components-and-class-components/) on the difference between functional and class components in React.

##### `/client/src/pages`
This directory contains full app pages, which likely leverage the custom components that you've created.

##### `/client/src/utilities`
This directory contains any non-component shared code you might need for your app. For example, you might have a `HelpText.tsx` file to keep all the help text for your app in one place. You might have a `NumberUtilties.tsx` file that has functions to standardize number formatting across your app.

##### `/client/src/App.tsx`
This files serves as the main component that will hold all your app contents/pages. For AI Core apps, the component returned in this file will need to wrap its content with `<UseInsights></UseInsights>` in order to utilize the SEMOSS SDK. Read more about it on [Front End Installation](../../Advanced%20Installation/Frontend%20Installation.md).
<!--As a front end newbie, I think it would be helpful to have a diagram that illustrates the relationship between the Semoss SDK, the App.tsx component, the pages that are in App.tsx, and the (smaller) components that are used on the pages-->

##### `/client/src/global.d.ts`
This file allows you declare global modules for import. For example, you might want to set up a module to import all .png files from your `/client/src/assets` folder so you can use the image somewhere in your app. (See an example of that [here](https://stackoverflow.com/questions/51100401/typescript-image-import))

##### `/client/src/main.tsx`
This file connects your App to the HTML template by looking for the root element in your template. This file is typically structured like so:
```
import React from 'react';
import ReactDOM from 'react-dom';

import { App } from './App';

ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('root'),
);
```

##### `/client/src/template.html`
This file serves as the entrypoint for HtmlWebpackPlugin to build the HTML files for your project. `template` is the standard name, but you can customize it and specify it in your plugin configuration in `webpack.config.js`. See the Html WebpackPlugin Usage [documentation](https://github.com/jantimon/html-webpack-plugin#usage) for details.

The `template.html` will contain an HTML element with an id of `root` that `main.tsx` will hook onto, for example:
```
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title><%= htmlWebpackPlugin.options.title %></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>

    <body>
        <div id="root"></div>
    </body>
</html>
```

### portals
This is the destination folder that should be configured as the [output](https://webpack.js.org/configuration/output/) in `webpack.config.js`. For example:
```
const config = {
    ...,
    output: {
        path: path.resolve(__dirname, '../portals'),
        filename: '[name].[contenthash].js',
        clean: true,
    },
    ...
```
All you have to do is have the folder. Running Webpack will do the rest! (More on that in the [Bundling Your App](#bundling-your-app) section.)

## Connecting to a AI Core Instance

See [Connecting to CFG](../Establish%20Connection%20to%20CFG%20Portal/Connecting%20to%20CFG%20AI.md) for documentation on how to set up your app with API keys to be added as environment variables.

## Using the SEMOSS SDK

Learn more about how to use the SEMOSS SDK in the [Using the SDK](../Establish%20Connection%20to%20CFG%20Portal/Using%20the%20SDK.md) guide.

## Local Development with Webpack

Ensure that you have the following script in your `client/package.json` file:
```
"scripts": {
    ...,
    "dev": "webpack serve",
    ...
  }
```

From the `client` directory, run the command `npm run dev` in your terminal. This will start the Webpack server for your local project. Here's an example of what it looks like when the script is executed:
```
PS C:\Development\cfgai-apps\llm-tuner\client> npm run dev

> dev
> webpack serve

<i> [webpack-dev-server] [HPM] Proxy created: /Monolith  -> https://workshop.cfg.deloitte.com/cfg-ai-demo
<i> [webpack-dev-server] Project is running at:
<i> [webpack-dev-server] Loopback: http://localhost:3000/, http://127.0.0.1:3000/
<i> [webpack-dev-server] Content not from webpack is served from 'C:\Development\cfgai-apps\llm-tuner\client\public' directory
<i> [webpack-dev-server] 404s will fallback to '/index.html'
asset main.5c6e6707538d8566f277.js 3.43 MiB [emitted] [immutable] [big] (name: main)        
asset logo.svg 1.27 KiB [emitted]
asset index.html 371 bytes [emitted]
orphan modules 1.82 MiB [orphan] 961 modules
runtime modules 30.4 KiB 15 modules
cacheable modules 2.5 MiB 423 modules
webpack 5.88.2 compiled successfully in 14852 ms
```

In this output, you can confirm that the proxy created matches the module and endpoint set up in your .env.local. If you navigate to [http://localhost:3000/](http://localhost:3000/) in your browser, you'll be able to see your app. So long as this task is running in your terminal, Webpack will watch for changes so they will appear in the browser when the changed file is saved (occasionally, this make take a while or you'll have to refresh the browser page). 

## Bundling Your App

Ensure that you have the following script in your `client/package.json` file:
```
"scripts": {
    ...,
    "build": "webpack --mode=production --node-env=production",
    ...
  }
```

From the `client` directory, run the command `npm run build` in your terminal. Webpack will bundle your app and the bundled files will automatically be added to your `/portals` directory. If you don't see any files populate that folder, then you likely have an issue with your Webpack configuration.

## Adding Your App to the AI Core App Catalog

1. Compress all the directories in your project _except_ your `/client` folder. Ensure that you do this within the app directory. For example, if my app files are in a folder called `/MyApp`, I should not zip `/MyApp` itself. I should navigate into `/MyApp` and zip all the relevant directories inside of it. 
2. Navigate to [App Library](https://workshop.cfg.deloitte.com/cfg-ai-demo/SemossWeb/packages/client/dist/) and click "Create a New App" in the top right corner. This will bring you to the Create New App page.
![Create New App](../../../static/img/AppUseCaseImages/app_building_options.png)
3. Click to "Get Started" under the Code option. Give your app a name and hit "Create." This will bring you to the Code Editor.
4. Click the Upload App Assets button at the top of the code file tree.
![Upload App Assets Button](../../../static/img/AppUseCaseImages/code_editor_asset_upload.png)
5. Attach your zip file in the modal that appears and hit "Upload."
![Upload App Assets Modal](../../../static/img/AppUseCaseImages/asset_upload_dialog.png)
6. Ta-da! You have successfully uploaded your code app to the AI Core Server!

## Updating your App In CFG

Once you have finished making any updates to your app, these are the steps that you should follow to update your app in CFG. 

1. Use the previous section to bundle your app correctly. 
2. Compress all the directories in your project _except_ your `/client` folder. Ensure that you do this within the app directory. For example, if my app files are in a folder called `/MyApp`, I should not zip `/MyApp` itself. I should navigate into `/MyApp` and zip all the relevant directories inside of it. 
3. Navigate to your App in the App Library. Click the Edit App Icon in the top right. 
![Edit App](../../../static/img/UpdateApp/demoAPP_edit.PNG)
4. Once in the Edit App tab, navigate to the Settings Tab. 
![Settings Tab](../../../static/img/UpdateApp/DemoApp_settings.PNG)
5. Once in the settings section, you want to go to the Data Apps Section. 
![Data Apps](../../../static/img/UpdateApp/DemoApp_data.PNG)
6. Once you are in Data Apps, you want to take the zip that you created in Step Two and upload it in the dropdown and press update! Once this is done your app will be fully updated. 
![Update App](../../../static/img/UpdateApp/DemoApp_update.PNG)

### What's Next?
Finished with this guide? 

Try out an App Use Case Quick Start guide for a different frontend framework linked below!
   - [Sample VanillaJS Use Case](VanillaJS%20App%20Quickstart%20Guide.md)
   - [Sample Streamlit Use Case](Streamlit%20App%20Quickstart%20Guide.md)
