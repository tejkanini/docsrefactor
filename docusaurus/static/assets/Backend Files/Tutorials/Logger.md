# Logger

Your custom pixel can use the `org.apache.logging.log4j.Logger` used in Semoss to debug, or log information

Access to the logger is provided in the `AbstractReactor.getLogger()` method. To use the logger you must pass in the class name of your custom reactor.


##  Example
The `LogMessagesReactor` shows examples of how to log a message passed in the pixel.

```
// imports...

public class LogMessagesReactor extends AbstractReactor {

	private static final String CLASS_NAME = LogMessagesReactor.class.getName();

	public LogMessagesReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.MESSAGE.getKey()};
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		String message = this.keyValue.get(this.keysToGet[0]);
		Logger logger = getLogger(CLASS_NAME);
		
		if (message == null || message.isEmpty()) {
			logger.info("Please add message to log");
		} else {
			logger.info(message);
		}
		
		// return boolean to indicate that the message has been added in the logger
		return new NounMetadata(true, PixelDataType.BOOLEAN);
	}
}

```
This reactor can be tested in the pixel console in SEMOSS by running:

```
 LogMessages(message=["hello"]);
```

Reference the [src/reactors/LogMessagesReactor.java](https://repo.semoss.org/semoss-training/backend/src/reactors) for the full implementation.