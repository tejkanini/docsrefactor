package reactors.query;
import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.selectors.QueryColumnSelector;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.om.task.BasicIteratorTask;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.Utility;

/**
 * This reactor shows how to add a relation to the SelectQueryStruct using @AddRelation
 * 
 * To test this reactor you need a database with the movie data.
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * ComplexQuery ( database = [ "<databaseId>" ]  ) ;
 */

/*
 * The SQL statement that we will be creating 
 * Select Title, Director, RottenTomatoes_Audience, RottenTomatoes_Critics, Genre from Title left join Director on Title.Title = Director.Title_FK left join Genre on Title.Title = Genre.Title_FK
 */
public class RelationQueryReactor extends AbstractReactor {

	public RelationQueryReactor() {
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
		qs.addSelector(new QueryColumnSelector("TITLE__Title"));
		qs.addSelector(new QueryColumnSelector("Director__Director"));
		qs.addSelector(new QueryColumnSelector("Director__Director"));
		qs.addSelector(new QueryColumnSelector("TITLE__RottenTomatoes_Audience"));
		qs.addSelector(new QueryColumnSelector("TITLE__RottenTomatoes_Critics"));
		qs.addSelector(new QueryColumnSelector("Genre__Genre"));
		
		// Joining on the Director Table 
		qs.addRelation("TITLE__Title", "Director__Title_FK", "left.join");
		qs.addRelation("TITLE__Title", "Genre__Title_FK", "left.join");
	
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
