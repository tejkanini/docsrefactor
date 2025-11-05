package reactors;

import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;

/**
 * This reactor shows examples of how to send messages as a response.
 * 
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * Messages(comment=["Keep going!"], level=["success"]);
 * Messages(comment=["try again"], level=["warning"]);
 * Messages(comment=["stop!!!!"], level=["error"]);
 */
public class MessagesReactor extends AbstractReactor {

	public MessagesReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.COMMENT_KEY.getKey(), "level" };
	}

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
}
