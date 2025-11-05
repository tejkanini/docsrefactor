package reactors.query;

import java.util.List;

import prerna.engine.api.IEngine;
import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.interpreters.sql.SqlInterpreter;
import prerna.query.querystruct.HardSelectQueryStruct;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.filters.GenRowFilters;
import prerna.query.querystruct.selectors.IQuerySort;
import prerna.query.querystruct.selectors.QueryColumnOrderBySelector;
import prerna.query.querystruct.selectors.QueryColumnOrderBySelector.ORDER_BY_DIRECTION;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.om.task.BasicIteratorTask;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.Utility;

/**
 * This reactor shows how to query a database using the @HardSelectQueryStruct.
 * 
 * To test this reactor you need a database with the movie data.
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * HardQuery ( database = [ "<databaseId>" ] , filters = [ Filter ( Title__Title == [ "Tangled" ] ) ] , 
 * sort = [ Sort ( columns = [ Title__MovieBudget ] , sort = [ "desc" ] ) ] ) ;
 */
public class HardQueryReactor extends AbstractReactor {

	public HardQueryReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), ReactorKeysEnum.SORT.getKey(), ReactorKeysEnum.FILTERS.getKey() };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		// get the database source
		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);

		// create the query structure and add components
		HardSelectQueryStruct qs = new HardSelectQueryStruct();
		
		// instead of selectors we will use native SQL
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT TITLE, MOVIEBUDGET, REVENUE_DOMESTIC ");
		query.append("FROM TITLE ");
		query.append("WHERE MOVIEBUDGET > 20000 ");
		
		
		StringBuilder queryModifications = new StringBuilder();
		
		// add filters passed in to the pixel command
		GenRowFilters additionalFilters = getFilters();
	
		// add sort option passed in
		List<IQuerySort> sorts = getSort();
		
		appendQueryModifications(queryModifications, database, additionalFilters, sorts);
		// add to the query 
		query.append(queryModifications.toString());
		// lastly you want to set the query to the query structure
		qs.setQuery(query.toString());
		
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


	protected void appendQueryModifications(StringBuilder query, IEngine app, GenRowFilters additionalFilters,
			List<IQuerySort> additionalSort) {
		appendQueryModifications(query, app, additionalFilters, additionalSort, true);
	}
	
	protected void appendQueryModifications(StringBuilder query, IEngine app, GenRowFilters additionalFilters, List<IQuerySort> additionalSort, boolean includeFirstAnd) {
		if(additionalFilters != null && !additionalFilters.isEmpty()) {
			boolean first = true;
			SqlInterpreter interp = (SqlInterpreter) app.getQueryInterpreter();
			SelectQueryStruct qs = new SelectQueryStruct();
			qs.setExplicitFilters(additionalFilters);
			interp.setQueryStruct(qs);
			interp.addFilters();
			List<String> queryFilters = interp.getFilterStatements();
			for(String qFilter : queryFilters) {
				if(first && includeFirstAnd) {
					query.append(" and ");
				} else if(!first) {
					query.append(" and ");
				}
				query.append(qFilter);
				first = false;
			}
		}
		if(additionalSort != null && !additionalSort.isEmpty()) {
			boolean first = true;
			for(int i = 0; i < additionalSort.size(); i++) {
				IQuerySort sort = additionalSort.get(i);
				if(sort.getQuerySortType() == IQuerySort.QUERY_SORT_TYPE.COLUMN) {
					QueryColumnOrderBySelector qSort = (QueryColumnOrderBySelector) sort;
					String tableCol = qSort.getQueryStructName().replace("__", ".");
					ORDER_BY_DIRECTION orderByDir = qSort.getSortDir();
					if(first) {
						query.append(" order by");
					}
					
					if(orderByDir == ORDER_BY_DIRECTION.ASC) {
						query.append(" ").append(tableCol).append(" ").append(" ASC ");
					} else {
						query.append(" ").append(tableCol).append(" ").append(" DESC ");
					} 
					if (i < additionalSort.size() - 1) {
						query.append(", ");
					}
					first = false;
				}
			}
		}
	}
}
