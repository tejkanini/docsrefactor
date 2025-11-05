package reactors.query;

import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.selectors.QueryColumnSelector;
import prerna.query.querystruct.selectors.QueryConstantSelector;
import prerna.query.querystruct.selectors.QueryFunctionHelper;
import prerna.query.querystruct.selectors.QueryFunctionSelector;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.om.task.BasicIteratorTask;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.Utility;

/**
 * This reactor shows how to call a function on a column to the SelectQueryStruct using @QueryFunctionSelector
 * 
 * To test this reactor you need a database with the movie data. This reactor
 * can be tested in the pixel console in SEMOSS by running: QueryFunction (
 * database = [ "<databaseId>" ] ) ;
 */

/*
 * The SQL statement that we will be creating Select Title, Count (Title), Group_Concat ( Coalesce ( RottenTomatoes_Critics ) )
 * 
 */
public class QueryFunctionReactor extends AbstractReactor {

	public QueryFunctionReactor() {
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
		qs.addSelector(new QueryColumnSelector("TITLE__TITLE"));
		// single query function selector
		{
			QueryFunctionSelector selectFun = new QueryFunctionSelector();

			selectFun.setAlias("Counts");
			selectFun.setFunction(QueryFunctionHelper.COUNT);
			selectFun.addInnerSelector(new QueryColumnSelector("TITLE__TITLE"));
			qs.addSelector(selectFun);
		}
		// double query function selectors
		{
			QueryFunctionSelector fun = new QueryFunctionSelector();
			fun.setFunction(QueryFunctionHelper.COALESCE);
			fun.addInnerSelector(new QueryColumnSelector("TITLE__ROTTENTOMATOES_CRITICS"));
			fun.addInnerSelector(new QueryConstantSelector(""));
			// perform group concat on newly coalesced column
			QueryFunctionSelector fun2 = new QueryFunctionSelector();
			fun2.setFunction(QueryFunctionHelper.GROUP_CONCAT);
			fun2.addInnerSelector(fun);
			fun2.setAlias("Critics");
			qs.addSelector(fun2);

			qs.addGroupBy(new QueryColumnSelector("TITLE__TITLE"));
		}

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
