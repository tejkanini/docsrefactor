package reactors.query;

import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.filters.SimpleQueryFilter;
import prerna.query.querystruct.selectors.QueryColumnSelector;
import prerna.query.querystruct.selectors.QueryConstantSelector;
import prerna.query.querystruct.selectors.QueryIfSelector;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.om.task.BasicIteratorTask;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.Utility;

/**
 * This reactor shows how to query a database using the @QueryIfSelector.
 * 
 * To test this reactor you need a database with the movie data.
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * CaseStatementQuery ( database = [ "<databaseId>" ] ) ;
 */
public class CaseStatementQueryReactor extends AbstractReactor {

	public CaseStatementQueryReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey() };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		// get the database source
		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);

		// create the query structure and add components
		SelectQueryStruct qs = new SelectQueryStruct();
		// add selectors by TABLE__COLUMN name
		qs.addSelector(new QueryColumnSelector("Title__Title"));
		qs.addSelector(new QueryColumnSelector("Title__RottenTomatoes_Audience"));
		// create movie recommendation column
		// case RottenTomatoes_Audience >= .8 then High
		// case RottenTomatoes_Audience >= .7 then Medium
		// case RottenTomatoes_Audience >= .5 then Low
		// ELSE "" as "Movie Recommendation"
		qs.addSelector(
			QueryIfSelector.makeQueryIfSelector(
				SimpleQueryFilter.makeColToValFilter("Title__RottenTomatoes_Audience", ">=", 0.8),
				new QueryConstantSelector("High"),
			QueryIfSelector.makeQueryIfSelector(
				SimpleQueryFilter.makeColToValFilter("Title__RottenTomatoes_Audience", ">=", .7),
				new QueryConstantSelector("Medium"),
			QueryIfSelector.makeQueryIfSelector(
				SimpleQueryFilter.makeColToValFilter("Title__RottenTomatoes_Audience", ">=", .5),
				new QueryConstantSelector("Low"), 
				new QueryConstantSelector(""), // Else no recommendation
				"Movie Recommendation"),
				"Movie Recommendation"),
				"Movie Recommendation")
		);

		// execute the query and process results in iterator
		IRawSelectWrapper iterator = null;
		try {
			iterator = WrapperManager.getInstance().getRawWrapper(database, qs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// create a task from the iterator
		BasicIteratorTask task = new BasicIteratorTask(qs, iterator);
		task.setNumCollect(-1);
		
		// return the task to get output
		return new NounMetadata(task, PixelDataType.FORMATTED_DATA_SET);

	}

}
