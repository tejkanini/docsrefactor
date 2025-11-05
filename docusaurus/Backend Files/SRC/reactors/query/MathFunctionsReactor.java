package reactors.query;
import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.selectors.QueryColumnSelector;
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
 * This reactor shows how to query using math functions 
 * 
 * To test this reactor you need a database with the movie data.
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * MathFucntions( database = [ "<databaseId>" ] ) ;
 */

/*
 * The SQL statement that we will be creating 
 * Select Director, Avg_Critic_Score, Total_Domestic_Rev, Total number of films directed per director 
 */
public class MathFunctionsReactor extends AbstractReactor {

	public MathFunctionsReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey()};
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
		qs.addSelector(new QueryColumnSelector("Director__Director"));

		// Lets create a Average Math Function to add back into sq
		QueryFunctionSelector averageFunction = new QueryFunctionSelector();
		averageFunction.setFunction(QueryFunctionHelper.AVERAGE_2);
		averageFunction.addInnerSelector(new QueryColumnSelector("Title__RottenTomatoes_Critics"));
		averageFunction.setAlias("Average_Critic_Rating");
		qs.addSelector(averageFunction);
		
		// Lets create a Sum Math Function to add back into sq
		QueryFunctionSelector sumFunction = new QueryFunctionSelector();
		sumFunction.setFunction(QueryFunctionHelper.SUM);
		sumFunction.addInnerSelector(new QueryColumnSelector("Title__Revenue_Domestic"));
		sumFunction.setAlias("Total_Domestic_Revenue");
		qs.addSelector(sumFunction);
		
		// Lets create a Total Math Function to add back into sq
		QueryFunctionSelector countFunction = new QueryFunctionSelector();
		countFunction.setFunction(QueryFunctionHelper.COUNT);
		countFunction.addInnerSelector(new QueryColumnSelector("Title__Title"));
		countFunction.setAlias("Total_Movies_Directed");
		qs.addSelector(countFunction);
		
		qs.addGroupBy("Director", "Director");
		// Joining on the title table for the info needed 
		qs.addRelation("Title__Title", "Director__Title_FK", "left.join");
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
