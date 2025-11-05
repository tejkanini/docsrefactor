package reactors;

import org.apache.logging.log4j.Logger;

import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;

/**
 * This reactor shows how to use the logger and integrate into custom reactors.
 * 
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * LogMessages(message=["hello"]);
 */
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