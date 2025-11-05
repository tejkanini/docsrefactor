package reactors;

import prerna.om.Insight;
import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import reactors.query.HardQueryReactor;

/**
 * This reactor shows how to call another reactor.
 * 
 * To test this reactor you need a database with the movie data. This reactor
 * can be tested in the pixel console in SEMOSS by running: 
 * CallHardQuery ( database = [ "<databaseId>" ] ) ;
 */
public class CallHardQueryReactor extends AbstractReactor {

	public CallHardQueryReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey() };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);

		return callHardQueryReactor(databaseId, this.insight);
	}

	private static NounMetadata callHardQueryReactor(String databaseId, Insight insight) {
		HardQueryReactor hardQueryReactor = new HardQueryReactor();
		hardQueryReactor.In();

		// 1 GenRowStruct per Reactor Key you are passing to the Reactor you want to call.
		GenRowStruct grs1 = new GenRowStruct();
		grs1.add(new NounMetadata(databaseId, PixelDataType.CONST_STRING));
		hardQueryReactor.getNounStore().addNoun(ReactorKeysEnum.DATABASE.getKey(), grs1);

		hardQueryReactor.setInsight(insight);
		NounMetadata retNoun = hardQueryReactor.execute();
		return retNoun;
	}
}
