---
sidebar_label: 'Custom Model Reactors'
slug: "java/custom-model-reactor"
description: "How to create a custom model reactor"
---

# Creating a Custom Model Reactor

## Overview

A reactor in AI Core is a modular unit of business logic designed to perform specific operations in response to user requests. Reactors encapsulate the core functionality needed to process input data, apply custom logic, and produce meaningful results for end users. In the context of this guide, you'll learn how to create a custom reactor that interacts with a Large Language Model (LLM) to generate jokes based on user input. This approach demonstrates how reactors can be extended to integrate advanced AI capabilities, enabling dynamic and engaging user experiences within AI Core.

## Custom Model Reactor Guide 

In this guide, you'll learn how to create a custom model reactor in AI Core that leverages a Large Language Model (LLM) to generate jokes based on user input. We'll walk through the process of building a `GenerateJoke` reactor, which takes user-provided text, constructs a prompt, and uses an LLM to produce a relevant joke.

Before proceeding, it is recommended that you are familiar with the basics of creating and running a custom reactor. For reference, please review the [Creating and Running a Custom Reactor guide](Custom%20Reactors.md).

This tutorial assumes you have a basic understanding of AI Core reactors and Java development. By the end, you'll know how to:

- Define a custom reactor class.
- Integrate an LLM model into your reactor.
- Accept user input and generate dynamic prompts.
- Return the generated joke as output.

Let's get started by setting up the custom reactor structure.

## Defining and Implementing a Custom Model Reactor

When working with LLMs or advanced model integrations, it is common to extend the `AskReactor` class, which provides additional features for handling prompts and model queries. The `AskReactor` constructor typically defines two main keys: `command` (the user’s question or instruction) and `fullPrompt` (an optional, fully-formed prompt for the model):

```java
private static final String FULL_PROMPT = "fullPrompt";

public AskReactor() {
    this.keysToGet = new String[] {ReactorKeysEnum.COMMAND.getKey(), FULL_PROMPT};
    this.keyRequired = new int[] {0, 0};
}
```

In the `execute()` method, you can access not only the direct user inputs, but also a rich set of model query parameters by retrieving a `ModelInferenceQueryStruct` object. This struct provides:
- **context:** Additional context or background information for the model.
- **hyperParameters:** A map of model-specific tuning parameters (e.g., temperature, max tokens).
- **engineId:** The identifier for the model engine to use.

Here’s how you might use these features in your reactor:

```java
@Override
public NounMetadata execute() {
    this.organizeKeys();
    ModelInferenceQueryStruct qs = getQueryStruct();

    IModelEngine model = Utility.getModel(qs.getEngineId());

    String question = this.keyValue.get(ReactorKeysEnum.COMMAND.getKey());
    String context = qs.getContext();
    Map<String, Object> hyperParameters = qs.getHyperParameters();
    Object fullPrompt = getFullPrompt();

    // Use question, context, hyperParameters, and fullPrompt as needed
    // Call the model and return the result as NounMetadata
    return ...;
}
```

The `getQueryStruct()` method ensures that your reactor can access the full query context, whether it is provided in the current row or in the general store. If a valid query struct is not found, it throws an error to prompt the user to supply the necessary information.

By leveraging these features, your custom reactor can support flexible user inputs, advanced prompt engineering, and fine-tuned model interactions, making it easy to build powerful AI-driven workflows in AI Core.

---

## AskReactor

A typical pattern for LLM integration is to extend the `AskReactor`, which handles prompt construction and model querying. Here’s a minimal version of the code:

```java
public class AskReactor extends AbstractReactor {
    private static final String FULL_PROMPT = "fullPrompt";

    public AskReactor() {
        this.keysToGet = new String[] {ReactorKeysEnum.COMMAND.getKey(), FULL_PROMPT};
        this.keyRequired = new int[] {0, 0};
    }

    @Override
    public NounMetadata execute() {
        this.organizeKeys();
        ModelInferenceQueryStruct qs = getQueryStruct();

        IModelEngine model = Utility.getModel(qs.getEngineId());

        String question = this.keyValue.get(ReactorKeysEnum.COMMAND.getKey());
        String context = qs.getContext();
        Map<String, Object> hyperParameters = qs.getHyperParameters();
        Object fullPrompt = getFullPrompt();

        if (question == null && fullPrompt == null) {
            throw new IllegalArgumentException("Please provide either an input using either command or fullPrompt.");
        }

        if (fullPrompt != null) {
            if (hyperParameters == null) {
                hyperParameters = new HashMap<>();
            }
            hyperParameters.put(AbstractModelEngine.FULL_PROMPT, fullPrompt);
        } else {
            question = Utility.decodeURIComponent(question);
        }

        Map<String, Object> output = model.ask(question, context, this.insight, hyperParameters).toMap();
        return new NounMetadata(output, PixelDataType.MAP, PixelOperationType.OPERATION);
    }

    // getFullPrompt() and getQueryStruct() methods omitted for brevity
}
```

---

## Minimal Example: GenerateJokeReactor

To implement a joke generator, extend `AskReactor` and customize the prompt logic:

```java
public class GenerateJokeReactor extends AskReactor {
    @Override
    public NounMetadata execute() {
        this.organizeKeys();
        String topic = this.keyValue.get(ReactorKeysEnum.COMMAND.getKey());
        if (topic == null) {
            throw new IllegalArgumentException("Please provide a topic using 'command'.");
        }
        String prompt = "Tell me a joke about " + topic + ".";
        this.keyValue.put("fullPrompt", prompt);
        return super.execute();
    }
}
```

**Summary:**  
- Extend `AskReactor` for LLM-based reactors.
- Use `command` or `fullPrompt` as input.
- For custom logic (like joke generation), build the prompt and delegate to the base reactor.

For more details, see the [AI Core documentation](Pixels.md).

