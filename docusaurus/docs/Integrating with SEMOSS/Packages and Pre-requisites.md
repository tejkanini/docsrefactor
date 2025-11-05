---
sidebar_label: "Packages and Prerequisites"
sidebar_position: 2
slug: "/prerequisites"
---

# Packages and Prerequisites for AI Core Apps

## Overview

In this section we will go through what front end and back end packages are necessary for most AI Core apps.

## Front End Prerequisities

For front end development the main prerequisities that you need are Node, pnpm, and the SEMOSS SDK.

### Node.js

You can install Node on your machine by downloading the appropriate installer for your device directly from the Node.js website. Version [v18.16.0](https://nodejs.org/download/release/v18.16.0/) is suggested for our projects (for most Windows machines, you should download `node-v18.16.0-x64.msi`).

### Code Editor

You'll need to have your code editor of choice downloaded on your machine. We suggest [Visual Studio Code](https://code.visualstudio.com/)

### Installing PNPM

You should have Node installed, that will be our package manager of choice. There are instructions for how to install this in our Prerequisites portion.

You can check that they exist on your machine by typing the following code in a terminal.

```
    node -v
    npm -v
```

If these output versions, that means that you have a working version of npm on your computer. If you do not go back to prerequisities and make sure to download Node (and npm alongside it).

We will be using npm to install pnpm, which is a lighter package manager.

To do so open a terminal and install it using:

`npm install -g pnpm`

#### Installing the SEMOSS SDK

The SEMOSS SDK includes software-building tools for a connecting AI Core to a custom React app. To use the SDK, ensure you have node, node package manager, and code editor installed.

Use PNPM/NPM to install the following packages and add them to your project.

`npm install @SEMOSS/sdk-react`

`npm install @SEMOSS/sdk`

### Project Structure

For a deep dive into the directory structure of your project, see the [React App Technical Guide](../App%20Creation%20Guides/React%20App%20In-Depth%20Guide.md).

### Optional: Fully Local Front End Installation

You **do NOT need a fully local front end installation** to build apps that run on AI Core.

However, if you wish to contribute to the AI Core project or want visibility into the front end source code, please follow this guide: [Frontend Local Installation Guide](../../Advanced%20Installation/Frontend%20Installation.md)

## Back End Prerequisities

Please Note that these are only necessary if you are planning on running a local back end. The majority of projects do not need to do so. Please refer to the [Docker BE Install Guide](../../Advanced%20Installation/Docker%20BE%20Install%20Guide.md) to see if you need to go through a back end installation.

### Back End Setup Overview

Though it is possible to develop apps solely with front end frameworks, AI Core allows you to incorporate custom back end logic using **Java** and **Python**. To get started with custom back end development, follow the [Java Setup](#java-setup) and [Python Setup](#python-setup) sections below.

**Optional - Web Server vs. Local Instance: Which should I choose?**

Next, choose whether you want to connect with the **web version** of AI Core, or if you want to run a **local instance** of AI Core instead.
Note that the primary way that most users will interact with the AI Core back end is through its **[live web server](https://workshop.cfg.deloitte.com/cfg-ai-demo/SemossWeb/packages/client/dist/#/)**. If you followed the previous section on [generating access and secret keys](./Connecting%20to%20CFG%20AI.md#generating-access-and-secret-keys), then you already can access this web server.

However, if you wish to have **admin privileges** or **greater control of an AI Core instance**, you can choose to run the AI Core server locally. AI Core provides two methods to run a AI Core instance locally, which are:

1. [Docker Container](#optional-starting-the-dockerized-back-end)
2. [Fully Local Installation](#optional-fully-local-back-end-installation)

### Java Setup

#### Java SE Development Kit 8u831 (JDK8) Download

> **Important** > _Please note that the specific edition of Java that AI Core requires is **Java SE Development Kit 8u381**. You MUST use this version of Java SE._

If you already have **Java SE Development Kit 8u831 (JDK8)** installed, skip to the next section: **[`JAVA_HOME` Environment Variable](#java_home-environment-variable)**.

1. Click on the following link to access the Java downloads page: [Java SE Development Kit (JDK8)​](https://www.oracle.com/java/technologies/downloads/#java8)
2. Scroll down to find **‘Java SE Development Kit 8u381’** and select the download that matches your computer's **OS and bit version.**
3. Accept License Agreement and download​
   - You will need to create an Oracle account using your email address​
4. Open up the file once it has finished downloading
5. Select next, next, next, all the way through;
   - By default, the installer will place Java into your `C:\Program Files` directory

#### `JAVA_HOME` Environment Variable

6. Open up a File Explorer and navigate to the `C:\Program Files\Java` folder. You should see a folder named `jdk-X.X`, where `X.X` refers to the JDK version you downloaded. Copy this full path from the file explorer (`C:\Program Files\Java\jdk-X.X`).
7. Create a new `JAVA_HOME` variable in your **System Environment variables** and paste the value of the path you copied in the previous step.
   > _**Don't know how to add a System Environment variable?** Learn how to do that in this [Windows guide](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html#GUID-DD6F9982-60D5-48F6-8270-A27EC53807D0) or this [Mac/OSX guide](https://phoenixnap.com/kb/set-environment-variable-mac#ftoc-heading-5) (follow the steps to create a PERMANENT variable)._
   - If the `JAVA_HOME` variable already exists in your system, confirm that it is pointing to the same path as the one you just copied.
8. In your System Environment Variables, edit your `PATH` variable to add `%JAVA_HOME%` as a new entry if it does not exist yet.
9. Click OK, the OK again to save the System Environment variables.
10. If you had any open IDEs or processes that use the `PATH` or `JAVA_HOME` variables, you will need to stop those processes and **restart them** to pull in the new paths.

### Maven Setup

#### Maven Download

If you already have **Apache Maven** installed, skip to the next section: **[`MVN_HOME` Environment Variable](#mvn_home-environment-variable)** .

1. Click on the following link to access the Maven downloads page: [Maven​](https://maven.apache.org/download.cgi)
2. Click the download link beside **Binary zip archive**, and unzip this to your `Documents` folder​

#### `MVN_HOME` Environment Variable

3. Open up a File Explorer and navigate to the Maven folder you unzipped. This may be at `C:\Users\your_username\Documents\apache-maven-#.#.#`.
   - `your_username` refers to the actual username you use on your computer
   - `#.#.#` refers to the Maven version number you downloaded.
4. Copy this path from the file explorer (`C:\Users\your_username\Documents\apache-maven-#.#.#`).
5. Create a new `MVN_HOME` path in your System Environment variables and paste the value of the path you copied in the previous step.
   > _**Don't know how to add a System Environment variable?** Learn how to do that in this [Windows guide](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html#GUID-DD6F9982-60D5-48F6-8270-A27EC53807D0) or this [Mac/OSX guide](https://phoenixnap.com/kb/set-environment-variable-mac#ftoc-heading-5) (follow the steps to create a PERMANENT variable)._
   - If the `MVN_HOME` variable already exists in your system, confirm that it is pointing to the same path as the one you just copied.
6. In your System Environment Variables, edit your `PATH` variable to add the following new entries if they do not exist yet:

- `%MVN_HOME%`
- `%MVN_HOME%\bin`

7. Click OK, the OK again to save the System Environment variables.
8. If you had any open IDEs or processes that use the `PATH` or `MVN_HOME` variables, you will need to stop those processes and **restart them** to pull in the new paths.

### Python Setup

The AI Server is currently running [Python 3.10.6](https://www.python.org/downloads/release/python-3106/). For consistency, we advise your local python environment be at least greater than 3.9.

#### Python 3.10.6 Download

If you already have **Python 3.9+** installed, skip to the next section: **[`PYTHONHOME` Environment Variable](#pythonhome-environment-variable)** .

1. Click on the following link to access the Python downloads page: [Python 3.10.6](https://www.python.org/downloads/release/python-3106/).
2. Locate the installer download that matches your computer's operating system and bit version, then download it.
3. Once the file is downloaded, open it and follow the steps in the installer.

#### `PYTHONHOME` Environment Variable

5. Once the installer has finished, open up a File Explorer and navigate to the `C:\Users\your_username\AppData\Local\Programs\Python` folder.
   - `your_username` refers to the actual username you use on your computer
6. You should see a folder named `Python3X`, where `X` refers to the Python version you downloaded. Copy this full path from the file explorer (`C:\Users\your_username\AppData\Local\Programs\Python\Python3X`).
7. Create a new `PYTHONHOME` path in your System Environment variables and paste the value of the path you copied in the previous step.
   > _**Don't know how to add a System Environment variable?** Learn how to do that in this [Windows guide](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html#GUID-DD6F9982-60D5-48F6-8270-A27EC53807D0) or this [Mac/OSX guide](https://phoenixnap.com/kb/set-environment-variable-mac#ftoc-heading-5) (follow the steps to create a PERMANENT variable)._
   - If the `PYTHONHOME` variable already exists in your system, confirm that it is pointing to the same path as the one you just copied.
8. In your System Environment Variables, edit your `PATH` variable to add the following new entries if they do not exist yet:

- `%PYTHONHOME%`
- `%PYTHONHOME%\Scripts`

8. Click OK, the OK again to save the System Environment variables.
9. If you had any open IDEs or processes that use the `PATH` or `PYTHONHOME` variables, you will need to stop those processes and **restart them** to pull in the new paths.

#### Python Packages Available within AI Core

AI Core currently comes bundled with the packages in the below list.

You can choose to install the relevant packages for your project from the below list by running the following command:
`pip install PACKAGE_NAME==VERSION_NUMBER`
where `PACKAGE_NAME` and `VERSION_NUMBER` are replaced with the actual name and version of the package you wish to install.

> **Note** > _`pip` is a package manager for Python. It typically is automatically installed alongside Python. If you prefer to use a different package manager such as `conda` instead, please feel free to do so._
>
> If you encounter a `pip is not recognized as an internal or external command` or `pip: command not found` error, it means that you either:
>
> - **Do not have pip installed:**
>   - **Solution**: Navigate to the `Scripts` folder in your `%PYTHONHOME%` directory (ex. `C:\Users\YOUR_USERNAME\AppData\Local\Programs\Python\Python310\Scripts`).
>
>     Confirm if there is a `pip` executable file in the directory.
>
>     If there is not, then please install `pip` by following this [guide](https://pip.pypa.io/en/stable/installation/).
> - **OR: Did not set your `%PYTHONHOME%\Scripts` path correctly in your System Environment variables:**
>
>   - **Solution**: Close your command line/terminal. Verify that the path you entered for your `%PYTHONHOME%` variable matches the path to your Python folder EXACTLY (ex. `C:\Users\YOUR_USERNAME\AppData\Local\Programs\Python\Python310\`).
>
>     Double check that your `%PATH%` contains a `%PYTHONHOME%\Scripts` entry.
>
>     Relaunch a new command line and try to run the `pip install` command again.

After installing the packages, you can view where they are installed by navigating to the `Lib` folder within your `PYTHON_HOME` directory (ex: `C:\Users\YOUR_USERNAME\AppData\Local\Programs\Python\Python310\Lib`).

If the following packages are not satisfactory for your application, then please email globalaicoreplatform@deloitte.com for support with additional packages.

```
accelerate==0.25.0
aiohttp==3.9.1
aiosignal==1.3.1
annotated-types==0.6.0
annoy==1.15.2
anyio==3.7.1
appdirs==1.4.4
async-timeout==4.0.3
attrs==23.1.0
backoff==2.2.1
beautifulsoup4==4.12.2
bitsandbytes==0.41.2.post2
blinker==1.7.0
boilerpy3==1.0.7
boto3==1.33.8
botocore==1.33.8
bs4==0.0.1
cachetools==5.3.2
cattrs==23.2.3
certifi==2023.11.17
charset-normalizer==3.3.2
click==8.1.7
cloudpickle==3.0.0
cmake==3.27.9
contourpy==1.2.0
cryptography==3.4.8
cycler==0.12.1
dask==2023.12.0
datasets==2.15.0
dbus-python==1.2.18
deepdiff==6.7.1
dill==0.3.7
distro==1.7.0
distro-info==1.1+ubuntu0.1
docopt==0.6.2
et-xmlfile==1.1.0
Events==0.5
exceptiongroup==1.2.0
faiss-cpu==1.7.4
farm-haystack==1.22.1
filelock==3.13.1
Flask==3.0.0
fonttools==4.46.0
frozenlist==1.4.0
fsspec==2023.10.0
fuzzywuzzy==0.18.0
google-api-core==2.14.0
google-auth==2.25.1
google-cloud-aiplatform==1.37.0
google-cloud-bigquery==3.13.0
google-cloud-core==2.3.3
google-cloud-resource-manager==1.10.4
google-cloud-storage==2.13.0
google-crc32c==1.5.0
google-resumable-media==2.6.0
googleapis-common-protos==1.61.0
greenlet==3.0.1
grpc-google-iam-v1==0.12.7
grpcio==1.59.3
grpcio-status==1.59.3
gunicorn==21.2.0
h11==0.14.0
httpcore==1.0.2
httplib2==0.20.2
httpx==0.25.2
huggingface-hub==0.19.4
idna==3.6
importlib-metadata==7.0.0
inflect==7.0.0
iniconfig==2.0.0
itsdangerous==2.1.2
jeepney==0.7.1
jep==3.9.1
Jinja2==3.1.2
jmespath==1.0.1
joblib==1.3.2
jsonpickle==3.0.2
jsonschema==4.20.0
jsonschema-specifications==2023.11.2
keyring==23.5.0
kiwisolver==1.4.5
launchpadlib==1.10.16
lazr.restfulclient==0.14.4
lazr.uri==1.0.6
lazy-imports==0.3.1
Levenshtein==0.23.0
lit==17.0.6
locket==1.0.0
loralib==0.1.2
MarkupSafe==2.1.3
matplotlib==3.8.2
monotonic==1.6
more-itertools==8.10.0
mpmath==1.3.0
multidict==6.0.4
multiprocess==0.70.15
networkx==3.2.1
nltk==3.8.1
num2words==0.5.13
numpy==1.26.2
nvidia-cublas-cu11==11.10.3.66
nvidia-cuda-cupti-cu11==11.7.101
nvidia-cuda-nvrtc-cu11==11.7.99
nvidia-cuda-runtime-cu11==11.7.99
nvidia-cudnn-cu11==8.5.0.96
nvidia-cufft-cu11==10.9.0.58
nvidia-curand-cu11==10.2.10.91
nvidia-cusolver-cu11==11.4.0.1
nvidia-cusparse-cu11==11.7.4.91
nvidia-nccl-cu11==2.14.3
nvidia-nvtx-cu11==11.7.91
oauthlib==3.2.0
openai==1.3.7
openpyxl==3.1.2
ordered-set==4.1.0
packaging==23.2
pandas==2.1.3
pandasql==0.7.3
partd==1.4.1
peft==0.6.2
Pillow==10.1.0
platformdirs==4.1.0
plotly==5.18.0
pluggy==1.3.0
posthog==3.1.0
prompthub-py==4.0.0
proto-plus==1.22.3
protobuf==4.25.1
psutil==5.9.6
pyarrow==14.0.1
pyarrow-hotfix==0.6
pyasn1==0.5.1
pyasn1-modules==0.3.0
pydantic==1.10.13
pydantic_core==2.14.5
PyGObject==3.42.1
pyjarowinkler==1.8
PyJWT==2.3.0
pyparsing==2.4.7
pytest==7.4.3
python-apt==2.4.0+ubuntu2
python-dateutil==2.8.2
python-Levenshtein==0.23.0
pytz==2023.3.post1
PyYAML==6.0.1
quantulum3==0.9.0
rank-bm25==0.2.2
rapidfuzz==3.5.2
referencing==0.31.1
regex==2023.10.3
requests==2.31.0
requests-cache==0.9.8
rpds-py==0.13.2
rsa==4.9
s3transfer==0.8.2
safetensors==0.4.1
scikit-learn==1.3.2
scipy==1.11.4
seaborn==0.13.0
SecretStorage==3.3.1
sentence-transformers==2.2.2
sentencepiece==0.1.99
shapely==2.0.2
six==1.16.0
sniffio==1.3.0
soupsieve==2.5
SQLAlchemy==2.0.23
sseclient-py==1.8.0
swifter==1.4.0
sympy==1.12
tabulate==0.9.0
tenacity==8.2.3
text-generation==0.6.1
threadpoolctl==3.2.0
tiktoken==0.5.2
tokenizers==0.15.0
tomli==2.0.1
toolz==0.12.0
torch==2.0.1
torchaudio==2.0.2
torchvision==0.15.2
tqdm==4.66.1
transformers==4.35.2
triton==2.0.0
typing_extensions==4.8.0
tzdata==2023.3
unattended-upgrades==0.1
url-normalize==1.4.3
urllib3==2.0.7
wadllib==1.3.6
Werkzeug==3.0.1
xlrd==2.0.1
xxhash==3.4.1
yarl==1.9.3
zipp==1.0.0
```

### Using the SEMOSS SDK

#### SEMOSS SDK Environment Variables

Note: We recommend manually setting the environment only in development mode.

You can setup a development environment and use access keys to authenticate with the app server. Generate the keys on the server and then update the Env module. See:

```
// import the module
import { Env } from '@SEMOSS/sdk';

// update the environment
Env.update({
    /**
     * Url pointing to the app server
     **/
    MODULE: '',
    /**
     * Access key generated by the app server
     **/
    ACCESS_KEY: '',
    /**
     * Secret key generated by the app server
     **/
    SECRET_KEY: '',
    /**
     * Optional field. This will load app specific reactors into the insight. Your app has to be hosted and running on the app server.
     **/
    APP: '',
});
```

> **Important** > _Please do not commit your keys, and do not place your keys in the `.env` file. Instead, externalize your keys to a `.env.local` file and load them in as environment variables during development. Additionally, **do not allow Git to track your `.env.local` file!** Your keys are SECRET, so you do not want to expose them by committing them to Git._

#### Powering Your App with Custom Python

The app server allows you to write custom Python to power your app. You can initialize your Python environment by:

**Loading via a file**

The SDK will load Python via an external file.

```
# ./hello.py
def sayHello(name):
    print(f'Hello {name}')
Set the option on initialize:
// import the module
import { Env } from '@SEMOSS/sdk';

// update the environment
insight.initialize({
    python: {
        /**
         *  Load the python via an external file
         **/
        type: 'file',
        /**
         *  Path to the file
         **/
        path: './hello.py',
        /**
         *  Alias for the file
         **/
        alias: 'smss',
    },
});
```

**Loading via js**

The SDK will load Python via an external file.

```
# ./hello.py

Set the option on initialize:
// import the module
import { Env } from '@SEMOSS/sdk';

// define it int he js
const py = `
def sayHello(name):
    printf('Hello {name}')
`;

// update the environment
insight.initialize({
    python: {
        /**
         *  Load the python via js
         **/
        type: 'script',
        /**
         *  Path to the file
         **/
        script: py,
        /**
         *  Alias for the file
         **/
        alias: 'smss',
    },
});

```

Next you can run the preloaded Python methods by calling the runPy action, like in the below example:

```
const hello = (name) => {
    const { pixelReturn } = await insight.actions.runPy(
        `smss.sayHello(${name})`,
    );

    // get the data
    const data = pixelReturn[0].output;

    console.log(data);
};
```

### Optional: Starting the Dockerized Back End

For users who wish to have **admin privileges** or want to host a **private instance** of AI Core, AI Core has made it possible for users to run AI Core locally by packaging its back end into a **Docker image**. The Docker image allows users to quickly start running AI Core inside of a container. To run the back end, please follow this guide: [Docker BE Install Guide](../../Advanced%20Installation/Docker%20BE%20Install%20Guide.md)

If you need **even greater control** over AI Core, including the ability to directly modify core AI Core source code or to bring your own dependencies, please follow the instructions in the next section (Optional: Fully Local Back End Installation).

### Optional: Fully Local Back End Installation

You **do NOT need to do a full local installation** in order to run AI Core locally. For most users, it is sufficient to simply download **Java** and **Python**, and then run the back end in a **Docker container** as noted in the previous section.

However, if you wish to **contribute to the AI Core project** or want **visibility into the back end source code**, please follow the instructions in this guide: [Local BE Install Guide](../../Advanced%20Installation/Local%20BE%20Install%20Guide.md).
