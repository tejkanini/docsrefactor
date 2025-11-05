# How to return pixel messages to the UI?
**Add the [RScriptReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/RScriptReactor.java) to follow along with this tutorial.**<br>


##  Creating the Reactor
This reactor duplicates and existing column and adds it to the frame. First we need to determine user inputs and specify these as Keys. In this example we will require that the user provides a column and a new column name.

```
	public RScriptReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.COLUMN.getKey(),
				ReactorKeysEnum.NEW_COLUMN.getKey() };
	}
```

##  Override execute() method
Just like creating any other reactor, we will override the execute() method from `AbstractReactor`. Firstly we will get the frame that was passed in. To Create a frame and data to it, please review [FrameCreateAndAddDataReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/FrameCreateAndAddDataReactor.java) to continue with this tutorial.

```
@Override
	public NounMetadata execute() {
		init();
		organizeKeys();
		
		// get frame
		RDataTable frame = (RDataTable) getFrame();
		String table = frame.getName();

		// get source column to duplicate
		String srcCol = this.keyValue.get(this.keysToGet[1]);

		// make sure source column exists
		String[] allCol = getColumns(table);
		if (srcCol == null || !Arrays.asList(allCol).contains(srcCol)) {
			throw new IllegalArgumentException("Need to define an existing column to duplicate.");
		}
```
##  Generating the R Script syntax
Next we will validate the new column and create the script. In order to run the custom R script we created, we now run it with the following method: `frame.executeRScript` Additionally we need to add it to the current insight by calling: `this.addExecutedCode(duplicate);`
```
		// clean and validate new column name or use default name
		String newColName = getCleanNewColName(frame, srcCol + "_DUPLICATE");
		String inputColName = this.keyValue.get(this.keysToGet[2]);
		if (inputColName != null && !inputColName.isEmpty()) {
			inputColName = getCleanNewColName(frame, inputColName);
			// entire new name could be invalid characters
			if (!inputColName.equals("")) {
				newColName = inputColName;
			}
		}

		// run script
		String duplicate = table + "$" + newColName + "<-" + table + "$" + srcCol + ";";
		frame.executeRScript(duplicate);
		this.addExecutedCode(duplicate);
```
## Validations and NounMetaData return
Lastly, we will be doing a quick validations check on the frame. The data at this point has the new column but it will not be reflected in semoss until we include metadata for it. We will now add the frame modifications to the metadata and then return the nounmetadata as the final step.
```
		// get src column data type
		OwlTemporalEngineMeta metaData = frame.getMetaData();
		String dataType = metaData.getHeaderTypeAsString(table + "__" + srcCol);
		if(dataType == null)
			return getWarning("Frame is out of sync / No Such Column. Cannot perform this operation");

		String adtlDataType = metaData.getHeaderAdtlType(frame.getName() + "__" + srcCol);

		// update meta data
		metaData.addProperty(table, table + "__" + newColName);
		metaData.setAliasToProperty(table + "__" + newColName, newColName);
		metaData.setDataTypeToProperty(table + "__" + newColName, dataType);
		if(adtlDataType != null && !adtlDataType.isEmpty()) {
			metaData.setAddtlDataTypeToProperty(frame.getName() + "__" + newColName, adtlDataType);
		}
		
		NounMetadata retNoun = new NounMetadata(frame, PixelDataType.FRAME, PixelOperationType.FRAME_HEADERS_CHANGE, PixelOperationType.FRAME_DATA_CHANGE);
		retNoun.addAdditionalReturn(new AddHeaderNounMetadata(newColName));
		return retNoun;
	}
```

Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for more examples.