# Pixel Inputs
*If you have any questions or suggestions regarding this tutorial, reach out to Ryan or Roxy.*

**Reference the [Input Reactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/basic-reactors/src/reactors/InputReactor.java) reactor to following along with this tutorial.**<br>
This reactor shows examples of how to define input parameters to be passed into the reactor, and how to retrieve those inputs once they're passed in from the frontend.


**This reactor can be tested in the pixel console in SEMOSS by running:**<br>
Input(name=["John"], comment=["Hello"], map=[{"test":"valueKey"}], age=[18], hasAccess=[true], colors=["red", "blue"]);
## Retrieving Pixel Inputs
Input parameters are used to pass data from the frontend to the backend for use within the reactor. Reactors define input parameters within the constructor. In this reactor, we use a mix of custom and generic keys as input parameters. The custom keys are defined either within the reactor or may be stored in a separate constants class for better organization. In the example below, we define custom input keys within the reactor, then set all the parameters in the constructor:
```java
        public static final String NAME_INPUT_KEY = "name";
	public static final String AGE_INPUT_KEY = "age";
	public static final String MAP_INPUT_KEY = "map";


	public InputReactor() {
		// keys can be custom or generic from the @ReactorKeysEnum
		this.keysToGet = new String[] { NAME_INPUT_KEY, ReactorKeysEnum.COMMENT_KEY.getKey() MAP_INPUT_KEY, "hasAccess", AGE_INPUT_KEY, "colors" };
	}
```
The input parameters are set on the frontend by referencing these parameters defined on the backend. Refer to the example pixel listed above to see this in action.<br>
## Retrieving Input
There are a few ways to retrieve the data that is passed in through the input parameters of a reactor. In all cases, we must first run the `organizeKeys();` method. This method gathers the frontend inputs and stores them in a map that we can access for use on the backend.
After this, we are free to define variables to reference the input data.
#### String Input
To retrieve simple string inputs, we can access the `keyValue` map:
```java
// index based on input key order defined in keysToGet
		String name = this.keyValue.get(this.keysToGet[0]);
		String comment = this.keyValue.get(this.keysToGet[1]);
```
#### Non-string input
To retrieve non-string values, we won't able to use this same method. Instead, we declare a `GenRowStruct` object and assign it the value of the input. Using the `this.store.getNoun()` method, we can retrieve the value from the store. Once this `GenRowStruct` object is defined, we attempt to cast it to a more specific type that we can manipulate. To do this, we run the `getValuesOfType()` method on our `GenRowStruct` object.  
In the example below, we are attempting to retrieve an input value as a **boolean:**
```java
		boolean input = false;
		GenRowStruct boolGrs = this.store.getNoun("hasAccess");
		if (boolGrs != null && !boolGrs.isEmpty()) {
			List<Object> val = boolGrs.getValuesOfType(PixelDataType.BOOLEAN);
			input = (boolean) val.get(0);
		}
		return input;
```
As is the case in this tutorial reactor, these processes are best done in separate methods to keep the execute method of the reactor clean and readable.  

After retrieving the input from the frontend, we are able to manipulate the data and return the desired output. In this tutorial, a string is built that contains all of the inputted data and is returned back to the frontend in the form of of a `NounMetaData` object.

## GenRowStruct
The `GenRowStruct`(General Row Structure) object is used as a generic type to retrieve a row of input data. With this object, we can use various methods to pass in data. Some of these methods include:
```java
        public void add(NounMetadata noun) {
		vector.addElement(noun);
	}
	
	public void addBoolean(Boolean bool) {
		add(bool, PixelDataType.BOOLEAN);
	}
	
	public void addDecimal(Double literal)
	{
		add(literal, PixelDataType.CONST_DECIMAL);
	}
```
After adding values, we can use methods to retrieve this info. The example below shows a method to get the actual values as well as a method to return a list of `NounMetadata` objects:
```java
public List<Object> getValuesOfType(PixelDataType type) {
		List<Object> retVector = new Vector<Object>();
		for(NounMetadata noun : vector) {
			if(noun.getNounType() == type) {
				retVector.add(noun.getValue());
			}
		}
		return retVector;
	}
	
	public List<NounMetadata> getNounsOfType(PixelDataType type) {
		List<NounMetadata> retVector = new Vector<NounMetadata>();
		for(NounMetadata noun : vector) {
			if(noun.getNounType() == type) {
				retVector.add(noun);
			}
		}
		return retVector;
	}
```
*To see what else the GenRowStruct object can do, be sure to visit it's page within the project*
