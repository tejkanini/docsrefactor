---
sidebar_label: "OpenAI Endpoints with Python"
sidebar_position: 5
slug: "/openai-python"
---

# Utilizing AI Core Models through OpenAI APIs

## 1. Overview

OpenAI is one of the pioneering companies within the AI landscape. As such, most packages ensure integration with the OpenAI API. Therefore, we have decided to follow suit and allow any AI Core Model Engines to be accessed via the OpenAI API.

We supposrt the following endpoints listed on the OpenAI website:

- [Chat Completions](https://platform.openai.com/docs/guides/text-generation/chat-completions-api)
- [Completions](https://platform.openai.com/docs/guides/text-generation/completions-api)
- [Embeddings](https://platform.openai.com/docs/guides/embeddings/what-are-embeddings)

## 2. Installation Guide

### Generate Access Key from your AI Platform

The first step to gaining access to the data stored in your AI Core platform is to generate an access key and a secret key. To do this, click on the link below. After you have generated your keys, navigate back to this guide.

[Generate Access Key](./Connecting%20to%20CFG%20AI.md#generating-access-and-secret-keys)

### Install the OpenAI and AI Platform Packages

A [new OpenAI Python](https://github.com/openai/openai-python/discussions/742) library was recently released. We provide examples of how to use the current major version (openai >= 1.0.0) and the old major version (openai \<= 0.28.1).

Open up a command prompt or terminal.
Then, run the following commands to install OpenAI Python API library and AI Platform Python library:

##### openai

```
pip install openai
pip install --upgrade ai-server-sdk
```

##### openai \<= 0.28.1

```
pip install openai<=0.28.1
pip install --upgrade ai-server-sdk
```

> **Note**
> This guide assumes that you already have Python (3.9+) and pip installed. If you do not, please follow the [Back End Setup](../../Advanced%20Installation/Local%20BE%20Install%20Guide.md) section first.

## 3. Using the AI Platform Endpoints with openai

In this walkthrough we will use existing code from the above endpoints and modify it to work with the AI Platform.

### Step 3.1: **Create a connection to the AI Platform**

Every api call that is made usually requires some level of authentication. First we store our credentials so that they only have to be passed once.

**Please note**: This step is required before moving to any other step.

```python
# import the ai platform package
import ai_server

# pass in your access and secret keys to authenticate
server_connection=ai_server.RESTServer(
    access_key="<your access key>",             
    secret_key="<your secret key>",            
    base="<your monolith api endpoint>"         # example: https://{domain}/{direcotry/path segment}/Monolith/api
)
```

### Step 3.2: **Change the default OpenAI endpoint to AI Platform OpenAI endpoint**

This Python code modifies the default endpoint in openai client and sets an API key. The API key is not required but the package requires one to be set.

```python
# import the openai package
from openai import OpenAI
import httpx as httpx
http_client = httpx.Client()
http_client.cookies=server_connection.cookies

# Modify OpenAI's API key and API base to use CFG's API.
client = OpenAI(
    api_key="EMPTY",
    base_url=server_connection.get_openai_endpoint(),
    default_headers=server_connection.get_auth_headers(),
    http_client=http_client
)
```

### Step 3.3: **Modifying the Code**

We are going to modify the existing examples for each of the endpoints above.

#### Chat Completions

```python
# original code
response = client.chat.completions.create(
    model="gpt-3.5-turbo",
    messages=[
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": "Who won the world series in 2020?"},
        {"role": "assistant", "content": "The Los Angeles Dodgers won the World Series in 2020."},
        {"role": "user", "content": "Where was it played?"}
    ]
)

# modified code
response = client.chat.completions.create(
    model="2c6de0ff-62e0-4dd0-8380-782ac4d40245", # change the model name to a Model Engine ID
    messages=[
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": "Who won the world series in 2020?"},
        {"role": "assistant", "content": "The Los Angeles Dodgers won the World Series in 2020."},
        {"role": "user", "content": "Where was it played?"}
    ],
    extra_body={"insight_id":server_connection.cur_insight} # add the insight_id as an extra param
)
```

#### Completions

```python
# original code
response = client.completions.create(
    model="gpt-3.5-turbo-instruct", # change the model name to a Model Engine ID
    prompt="Write a tagline for an ice cream shop."
)

# modified code
response = client.completions.create(
    model="4801422a-5c62-421e-a00c-05c6a9e15de8", # change the model name to a Model Engine ID
    prompt="Write a tagline for an ice cream shop.",
    extra_body={"insight_id":server_connection.cur_insight} # add the insight_id as an extra param
)
```

#### Embeddings

```python
# original code
client.embeddings.create(
    input=["Your text string goes here"],
    model="text-embedding-ada-002",
)

# modified code
client.embeddings.create(
    input=["Your text string goes here"],
    model="cb661f04-cb30-48fd-aafc-b4d422ce24e4",
    extra_body={"insight_id":server_connection.cur_insight} # add the insight_id as an extra param
)
```

### 4 Langchain with openai

```python
from langchain_openai import ChatOpenAI
from langchain.prompts.chat import (
    ChatPromptTemplate,
    HumanMessagePromptTemplate,
    SystemMessagePromptTemplate,
)
from langchain.schema import HumanMessage, SystemMessage

import httpx as httpx
http_client = httpx.Client()
http_client.cookies=server_connection.cookies

chat = ChatOpenAI(
    api_key="EMPTY",
    base_url=server_connection.get_openai_endpoint(),
    default_headers=server_connection.get_auth_headers(),
    extra_body={"insight_id":server_connection.cur_insight},
    http_client=http_client,
    model="2c6de0ff-62e0-4dd0-8380-782ac4d40245",
)

messages = [
    SystemMessage(
        content="You are a helpful assistant that translates English to French."
    ),
    HumanMessage(
        content="Translate this sentence from English to French. I love programming."
    ),
]
print(chat(messages))
```
