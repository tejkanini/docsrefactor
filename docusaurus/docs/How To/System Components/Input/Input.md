---
sidebar_label: 'Input Block'
sidebar_position: 6
slug: '/input-block'
---

# Input Block

You can add an Input Block to allow users to enter various types of data. Follow the steps below to add an Input Block to your app UI.

## Block Settings

### Content

Make sure you are in the 'Block' tab (inside the 'Dev' tab) on your page. The Dev section and the Block section are highlighted in the image below.

![Block](../../../../static/img/Systemcomponents/Block.png)

Scroll down the various block options until you reach 'Input' (under the heading of Input). The Input block is highlighted in the image below.

<!-- ![Input](../../../../static/img/SystemComponents/Input.png) -->

Drag and drop the **Input** Block into your App UI. Once you drop the block in your UI, you will see various parameters for which data needs to be populated on the right side of the screen. If you do not see this, click on **Block Settings**. The Block Settings tab is highlighted in the image below.

![Block Settings](../../../../static/img/Systemcomponents/Blocksettings.jpg)

### Parameters

#### Show Block

Toggle this option to show or hide the block.

#### Value

Enter the default value for the input field.

#### Label

Specify the label for the input field.

#### Hint

Provide a hint that will appear below the input field.

#### Type

Select the type of input from options like Text, Number, Date, etc.

#### Rows

Specify the number of rows for the input field (applicable for text areas).

#### Loading

Attach a query to load data dynamically into the input field.

#### Disabled

Toggle this option to enable or disable the input field.

#### Required

Toggle this option to make the input field mandatory.

### On Change

We want our options to be visible as soon as the user loads the corresponding page. Thus we will create an 'On Change' entry to enable this.

Click on '+ New Action' under On Change. Select 'Run Query' under Type and 'Options' under Query.

Click on 'Save' to finish.

<!-- ![Input Block](../../../../../static/img/SystemComponents/InputBlock.png) -->

Congratulations, you have successfully added an Input Block to your UI.
