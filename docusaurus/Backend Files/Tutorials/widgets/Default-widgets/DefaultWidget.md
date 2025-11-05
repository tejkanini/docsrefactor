# Default Widgets
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) to follow along with this tutorial.**<br>

## Setting up the JSON Widget
The JSON for creating a custom widget should consist of the following elements shown below. (A complete blank JSON example is found at the end of this document.)
```
[
    {
      "label": "Widget Name",
      "description": "Instructions for the Widget",
      "execute": "button",
      "listeners": [],
      "query": "",
      "params": []
    }
]
```

## Widget Definitions
- Label: a STRING specifying the label for the widget 
- Description: a STRING containing a description for the widget (such as instructions) to be displayed at the top of the widget UI.
- Execute: a STRING set to either "button" or "auto", button will show a button the user clicks to execute the widget whereas "auto" will automatically execute the widget - once it is built/rendered.
- Listeners: an ARRAY[] listing the names of ‘listeners’ or commands that will trigger the widget to refresh. 
- Listeners will not trigger the widget to re-execute, but will simply refresh the values of any parameters specified in the widget. This property is not required but is useful to ensure parameter options / values react and reset dynamically. Some common listeners include but are not limited to:
```
-	updateTask: a new task has been added – e.g. new visualization created
-	updateFrame: the frame has changed
-	addedData: new data has been added to the frame
-	alterDatabase: database has changed
-	selectedData: selected data has changed
```
- Query: a STRING containing the pixel command that will run when the widget is executed. 
  
  For example, if you wanted to build a widget to add a new column to your frame the query could look like:
```
"query": "<SMSS_FRAME.name> | AddColumn(newCol=[\"<newColName>\"],dataType=[\"<newColType>\"]);"
```

- Note that any variables referenced in the query are put inside carrots for example, <variable_name>. In the query above we reference 3 variables
```
-	<SMSS_FRAME.name>: the name of our frame
-	<newColName>: a parameter we will define in the "params" element of the JSON (see below) to hold the user inputted text name for the new column
-	<newColType>: a parameter we will define in the "params" element of the JSON to hold the user selected data type of the new column
```

- Params: an ARRAY[] defining the parameters to use in the widgets pixel query above. Each parameter object {} in the array can have the following properties shown below:

```
[{
    "label": "Widget Name", 
    "description": "Instructions for the widget",
    "execute": "button",
    "listeners": [],
    "query": "", 
    "params": [{
        "paramName": "", 
        "required": true, 
        "view": {},
        "model": {}
    }]
}]
```

- paramName: a STRING defining the name of the parameter used in the pixel query
- required: BOOLEAN true or false is this param required to be filled in to execute the pixel query
- view: an OBJECT {} storing properties about how to display this parameter component on the UI. If the parameter is only used in the query but we do not need to display a component on the UI, view is set to false. If the parameter requires user input and will be displayed as a field on the UI, for example asking the user to enter the name of a new column, the view object{} can store the following properties shown below:
```
[{
    "label": "Widget Name", 
    "description": "Instructions for the widget",
    "execute": "button",
    "listeners": [],
    "query": "", 
    "params": [{
        "paramName": "", 
        "required": true, 
        "view": { 
            "displayType": "",
            "label": "",
            "description": "",
            "attributes": {}
        },
        "model": {}
    }]
}]
```

- displayType: a STRING specifying which type of field/component to create on the UI for this parameter. Options include:
```
-	"checklist" – checklist of options
-	"freetext" – a blank text field
-	"number" – a number field
-	"color-picker" – select color from theme color options
-	"dropdown" – list of options to select from
-	"typeahead" – text field where inputted text will filter dropdown list of options that match 
-	"checkbox" – value will be stored as true or false
-	"multiselect" – similar to checklist but always allows multiple selection and displays the selected options inside the text field
-	"slider" – slide bar to select value/range
-	"date-picker" – select a date from a calendar
-	"button" – display options as selectable buttons
```
- label: STRING the label to display above this parameter component on the UI
- description: STRING the description to show below this field component, such as instructions
- attributes: an OBJECT {} holding specific attributes to assign to this component. These attributes will vary based on the displayType of the component as explained in each attribute property’ definition below. 
```
[{
    "label": "Widget Name", 
    "description": "Instructions for the widget",
    "execute": "button",
    "listeners": [],
    "query": "", 
    "params": [{
        "paramName": "", 
        "required": true, 
        "view": { 
            "displayType": "",
            "label": "",
            "description": "",
            "attributes": {
                "searchable": true, 
                "multiple": true,
                "quickselect": true,
                "display": "",
                "value": "",
                "readonly": true,
                "step": 0.5,
                "label": "", 
                "direction": ""
            }
        }
     }]
```
-	searchable: a BOOLEAN true or false to add a search box to a component 
```
	component types: checklist, dropdown, multiselect
```

-	multiple: a BOOLEAN true or false to allow multiple options to be selected (false would only allow single selection)
```
	component types: all component types except for "button"
```

-	quickselect: a BOOLEAN true or false to add a Select All option to a component
```
	component types: checklist, dropdown, multiselect
```

-	display: a STRING if the default options to select from is a list of objects instead of a list of strings/numbers this attribute is needed to define what property inside the object to be used to display on the UI.

For example, if the following array of objects is defined as the defaultOptions for a parameter (defaultOptions is a property of "model" discussed below), we would specify "display": "displayValue".
```
'defaultOptions': 
	[
        {
            'value': 'false',
            'displayValue': 'No'
        },
        {
            'value': 'true',
            'displayValue': 'Yes'
        }
    ]
```
-	value: a STRING if the default options to select from is a list of objects instead of a list of strings/numbers this attribute is needed to define what property inside the object to be used as the value to store in the parameter and use in the widget query when executed. In the above example, when a user selects "Yes" the widget query will execute with the parameter value equal to ‘true’

-	readonly: BOOLEAN true or false can the value of this parameter be changed (false would make the value static)

-	step: NUMBER to use as the increment for number fields where arrows allow the user to increase / decrease the entered value by the step size
```
	component types: number 
```

-	label: STRING text to display next to checkbox component
```
	component types: checkbox
```

-	direction: STRING "left" "right" or "center" specifying where on the UI to place a checkbox component
```
	component types: checkbox
```

- model: an OBJECT {} storing properties about the value of the parameter. The model object can have any of the properties shown below, but which properties are required will vary component by component. 
```
[{
    "label": "Widget Name", 
    "description": "Instructions for the widget",
    "execute": "button",
    "listeners": [],
    "query": "", 
    "params": [{
        "paramName": "", 
        "required": true, 
        "view": {},
        "model": {
            "query": "",
            "valueQuery": "",
            "infiniteQuery": "",
            "searchParam": "",
            "dependsOn": [],
            "autoSelect": true,
            "defaultValue": "", 
            "defaultOptions": [], 
            "accessor": "",
            "useSelectedValues": true,
            "replaceSpacesWithUnderscores": true, 
            "replaceEmptyWith": "",
            "link": "",
            "filter":"",
            "min": 1,
            "max": 5 
        }
    }]
}]
```
-	query: a STRING specifying the pixel to execute to populate the parameters options. This is only required for component types that require options to select from (for e.g. checklist, dropdown, multiselect, require options to select from whereas freetext does not). 
Alternatively, options can be set manually using "defaultOptions" below
-	defaultOptions: an ARRAY[] of options to populate the component with. This can be a list of string values or numbers, or can be a list of objects such as the example below:
```
'defaultOptions': [
        {
            'value': 'false',
            'displayValue': 'No'
        },
        {
            'value': 'true',
            'displayValue': 'Yes'
        }
 ]
```

-	defaultValue: any type allowed (number, string, Boolean, array) specifies what value to populate the component with when it is first created/loaded. It can also be left blank such as ‘defaultValue’: ‘’ or omitted entirely if the parameter has no options such as a simple text field. 

-	valueQuery: a STRING specifying the pixel command to run to get the value/s to set as the default for this parameter. This property is not required but allows the default value to be set dynamically.

-	infiniteQuery: a STRING specifying the pixel command to run when the list of options for a component is very large. For example if I have a checklist component with options for every city in the United states, the model query will pull the first <x> number of options and as the user scrolls down the checklist passed the xth option, additional options will be pulled using the infinite query. 

This property is not required but prevents increased loading time if the number of options for a component is significantly large

-	searchParam: a STRING specifying the name of a search parameter used in the model query if one exists. 
For example, if I create a checklist component with a search box and I want the checklist options to be filtered by my search criteria, my model query and search param could be defined as below:
```
'model': {
    'query': 'Frame(<SMSS_FRAME.name>) | Select(<columnName>) | Filter(<columnName> ?like "<search>") | Collect(50);',
    'searchParam': 'search'
}
```
-	dependsOn: an ARRAY[] listing any other parameters used in the model query for this parameter (i.e. any other parameters that this parameter depends on).
The model query for the current parameter will not execute until the dependent parameters’ values have been set. For example, in the checklist component above, the model query depends on 2 other parameters, ‘columnName’ and ‘search’. 

```
'model': {
    'query': 'Frame(<SMSS_FRAME.name>) | Select(<columnName>) | Filter(<columnName> ?like "<search>") | Collect(50);',
    'searchParam': 'search',
    'dependsOn': [
        'columnName',
        'search'
    ]
}
```

-	autoSelect: BOOLEAN true or false whether to select the first option in the defaultOptions list to be the default value when the component is built/refreshed

-	useSelectedValues: BOOLEAN true or false to use the values that are selected in the UI. This property is not required but is mainly used for checklist components when a ‘Select All’ option is added. This property should NOT be used if the list of component options is very large i.e. when an infiniteQuery is used.

-	replaceSpacesWithUnderscores: BOOLEAN true or false to replace any spaces with underscores in the selected parameter values when running the pixel query. 

-	replaceEmptyWith: any type (string, number, etc) what to assign the parameter value to when executing the widget if no value is selected or entered into this parameter. 

This property is not required but is only used for parameters that are not required as required parameters will never have an empty value.

-	link: a STRING specifying a parameter who’s value to copy/auto assign this parameters value to when the linked parameter value changes. This property is optional and should only be used when you want 2 parameters to share the same values.

-	filter: a STRING specifying any additional filter commands to run on the parameters value before executing the widget pixel command. For example if you need to convert any parameter values from camel case to title case,  add the property ‘filter’: ‘camelCaseToTitleCase’

-	min: a NUMBER specifying the minimum parameter value allowed for number components only

-	max: a NUMBER specifying the maximum parameter value allowed for number components only

-	accessor: a STRING needed for any reactors that return any special objects that need to be traversed through to grab the values. This property is not required and is rarely used.
```
[{
    "label": "Widget Name", 
    "description": "Instructions for the widget",
    "execute": "button",
    "listeners": [],
    "query": "", 
    "params": [{
        "paramName": "", 
        "required": true, 
        "view": { 
            "displayType": "",
            "label": "",
            "description": "",
            "attributes": {
                "searchable": true, 
                "multiple": true,
                "quickselect": true,
                "display": "",
                "value": "",
                "readonly": true,
                "step": 0.5,
                "label": "", 
                "direction": ""
            }
        },
        "model": {
            "query": "",
            "valueQuery": "",
            "infiniteQuery": "",
            "searchParam": "",
            "dependsOn": [],
            "autoSelect": true,
            "defaultValue": "", 
            "defaultOptions": [], 
            "accessor": "",
            "useSelectedValues": true,
            "replaceSpacesWithUnderscores": true, 
            "replaceEmptyWith": "",
            "link": "",
            "filter":"",
            "min": 1,
            "max": 5 
        }
    }]
}]
```

### Complete JSON Widget Example
Example param definitions for each of the displayType options
```
// checklist - clean-upper-case
{
	'paramName': 'upperCaseCols',
	'view': {
		'displayType': 'checklist',
		'label': 'Select column(s)',
		'description': 'Note: Selected numerical/date columns will converted into a string.',
		'attributes': {
			'display': 'alias',
			'value': 'alias',
			'multiple': true,
			'quickselect': true,
			'searchable': true
		}
	},
	'model': {
		'query': '<SMSS_FRAME.name> | FrameHeaders(headerTypes=["STRING"]);'
	},
	'useSelectedValues': true
}


// dropdown - clean-extract-numbers-letters
{
	'paramName': 'override',
	'view': {
		'displayType': 'dropdown',
		'label': 'Create new column(s) with results:',
		'description': "'No' will override the existing column values with the results.",
		'attributes': {
			'display': 'display',
			'value': 'value'
		}
	},
	'model': {
		'defaultOptions': [
			{
				'value': 'true',
				'display': 'No'
			},
			{
				'value': 'false',
				'display': 'Yes'
			}
		],
		'defaultValue': 'false',
		'replaceSpacesWithUnderscores': true,
		'filter': 'camelCaseToTitleCase'
	}
}


// number - analytics-associated-learning
{
	'paramName': 'conf',
	'view': {
		'displayType': 'number',
		'label': 'Confidence (Between 0 and 1):',
		'attributes': {
			'multiple': false,
			'step': 0.01
		}
	},
	'model': {
		'defaultValue': 0.8,
		'min': 0,
		'max': 1
	},
	'required': true
},


// freetext - chart-title
{
	'paramName': 'fontFamily',
	'view': {
		'displayType': 'freetext',
		'label': 'Set a font style:'
	},
	'model': {
		'defaultValue': '<SMSS_SHARED_STATE.chartTitle.fontFamily>',
		'defaultOptions': []
		'replaceSpacesWithUnderscores': true
	},
	'required': true
}


// color-picker - chart-title
{
	'paramName': 'fontColor',
	'view': {
		'displayType': 'color-picker',
		'label': 'Choose a font color:'
	},
	'model': {
		'defaultValue': '<SMSS_SHARED_STATE.chartTitle.fontColor>',
		'defaultOptions': []
	},
	'required': true
},


// checkbox - clean-to-percent
{
	'paramName': 'by100',
	'view': {
		'displayType': 'checkbox',
		'attributes': {
			'label': 'Check if you would to multiply the value by 100',
			'direction': 'left'
		}
	},
	'model': {
		'defaultValue': false
	}
},


// typeahead - clean-drop-rows
{
	'paramName': 'value',
	'view': {
		'displayType': 'typeahead',
		'label': 'Enter value to compare against:',
		'description': 'Rows containing a match will be removed.'
	},
	'model': {
		'dependsOn': [
			'column',
			'search'
		],
		'defaultValue': '',
		'query': '(dropRowValues = Frame(<SMSS_FRAME.name>) | Select(<column>) | Filter(<column> ?like "<search>") | Sort(cols=[<column>], direction=[asc]) | Iterate()) | Collect(50);',
		'infiniteQuery': 'dropRowValues | Collect(50);',
		'searchParam': 'search'
	},
	'required': false
}


// no display type, create search param - clean-drop-rows
{
	'paramName': 'search',
	'view': false,
	'model': {
		'defaultValue': ''
	}
},
```