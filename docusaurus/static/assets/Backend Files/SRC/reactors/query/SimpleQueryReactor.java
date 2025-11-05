package reactors.query;

import java.util.List;

import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.filters.GenRowFilters;
import prerna.query.querystruct.filters.SimpleQueryFilter;
import prerna.query.querystruct.selectors.IQuerySort;
import prerna.query.querystruct.selectors.QueryColumnSelector;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.om.task.BasicIteratorTask;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.Utility;

/**
 * This reactor shows how to query a database using the @SelectQueryStruct.
 * 
 * To test this reactor you need a database with the movie data.
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * SimpleQuery ( database = [ "<databaseId>" ] , limit = [ 10 ] , offset = [ 5 ] , filters = [ Filter ( Title__Title == [ "Avatar" ] ) ] , 
 * sort = [ Sort ( columns = [ Title__Movie_Budget ] , sort = [ "desc" ] ) ] ) ;
 */
public class SimpleQueryReactor extends AbstractReactor {

	public SimpleQueryReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), ReactorKeysEnum.SORT.getKey(),
				ReactorKeysEnum.LIMIT.getKey(), ReactorKeysEnum.OFFSET.getKey(), ReactorKeysEnum.FILTERS.getKey() };
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
		qs.addSelector(new QueryColumnSelector("Title__MovieBudget"));
		qs.addSelector(new QueryColumnSelector("Title__Revenue_Domestic"));

		// add filters
		qs.addExplicitFilter(SimpleQueryFilter.makeColToValFilter("Title__MovieBudget", ">", 20000));
		
		// add filters passed in to the pixel command
		GenRowFilters additionalFilters = getFilters();
		if (additionalFilters != null) {
			qs.mergeExplicitFilters(additionalFilters);
		}
	
		// add sort option passed in
		List<IQuerySort> sorts = getSort();
		if (sorts != null) {
			qs.setOrderBy(sorts);
		}
		
		// add limit and offset options passed in
		qs.setLimit(getLimit());
		qs.setOffSet(getOffset());

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

	protected GenRowFilters getFilters() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.FILTERS.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata filterNoun = inputsGRS.getNoun(0);
			SelectQueryStruct qs = (SelectQueryStruct) filterNoun.getValue();
			GenRowFilters filters = qs.getCombinedFilters();
			return filters;
		}
		return null;
	}

	protected List<IQuerySort> getSort() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.SORT.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata sortNoun = inputsGRS.getNoun(0);
			SelectQueryStruct qs = (SelectQueryStruct) sortNoun.getValue();
			List<IQuerySort> orderBy = qs.getOrderBy();
			return orderBy;
		}
		return null;
	}

	protected int getLimit() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.LIMIT.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata limitNoun = inputsGRS.getNoun(0);
			return ((Number) limitNoun.getValue()).intValue();
		}
		return -1;
	}

	protected int getOffset() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.OFFSET.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata offsetNoun = inputsGRS.getNoun(0);
			return ((Number) offsetNoun.getValue()).intValue();
		}
		return -1;
	}

}
