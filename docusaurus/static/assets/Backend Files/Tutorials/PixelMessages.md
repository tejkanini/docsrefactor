# How to return pixel messages to the UI?
**Add the [MessagesReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/MessagesReactor.java) to follow along with this tutorial.**<br>


##  Creating the Reactor
First we need to determine user inputs and specify these as Keys. In this example we will require that the user provides a comment and the message level. Note: message levels can only be success, warning or error.

```
	public MessagesReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.COMMENT_KEY.getKey(), "level" };
	}
```

##  Override execute() method
Just like creating any other reactor, we will override the execute() method from `AbstractReactor`. We want to end this method with returning the NounMetadata with the desired noun message and level.

```
	@Override
	public NounMetadata execute() {
		organizeKeys();
		String comment = this.keyValue.get(this.keysToGet[0]);
		String level = this.keyValue.get(this.keysToGet[1]);
		if (level != null) {
			if (level.equalsIgnoreCase("SUCCESS")) {
				return NounMetadata.getSuccessNounMessage(comment);
			} else if (level.equalsIgnoreCase("WARNING")) {
				return NounMetadata.getWarningNounMessage(comment);
			} else if (level.equalsIgnoreCase("ERROR")) {
				return NounMetadata.getErrorNounMessage(comment);
			}
		}
		NounMetadata noun = new NounMetadata("Please define a level", PixelDataType.CONST_STRING);
		noun.addAdditionalReturn(NounMetadata.getSuccessNounMessage("set level = [SUCCESS] to display this way"));
		noun.addAdditionalReturn(NounMetadata.getWarningNounMessage("set level = [WARNING] to display this way"));
		noun.addAdditionalReturn(NounMetadata.getErrorNounMessage("set level = [ERROR] to display this way"));
		return noun;
	}
```

Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for more examples.