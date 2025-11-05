# HardSelectQueryStruct
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [HardQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/HardQueryReactor.java) to follow along with this tutorial.**<br>
To query a database in SEMOSS use the HardSelectQueryStruct object. The HardSelectQueryStruct holds all the raw SQL query information for the query with the ability to also use custom passed-on pixel arguments for filters, joins, order by, etc.

```
	HardSelectQueryStruct qs = new HardSelectQueryStruct();
```


## Adding Selectors
To specify which column you want to select, instead of selectors you need to add your column using native SQL. We will start by using a StringBuilder for our query.

```
		StringBuilder query = new StringBuilder();
		query.append("SELECT TITLE, MOVIEBUDGET, REVENUE_DOMESTIC ");
		query.append("FROM TITLE ");
```

## Adding Filters
You can add filters by the append method. In this example we are only selecting titles that have a Movie_Budget > 2000. This filter will get added everytime the code gets executed.
```
	query.append("WHERE MOVIEBUDGET > 20000 ");

```

You can add dynamic filters passed in the pixel by grabbing the filter input.
```
	GenRowFilters additionalFilters = getFilters();
```

## Adding Sort
You can also add a sort by grabbing the sort input.
```
	// add sort option passed in
	List<IQuerySort> sorts = getSort();
```

## Getting the Results
Finally once the query structure is created, we add query modifications based on the filters and sort passed in. Finally, we can execute the query and return the results. In a `HardSelectQueryStruct`, you want to set the string query we created above to the HardSelectQuery structure. The query is executed on the database and an iterator is provided. Semoss provides a `BasicIteratorTask` to create the task payload.

```
	// first we want to add the dynamic query modifications from user input to the query we created above.
	StringBuilder queryModifications = new StringBuilder();
	appendQueryModifications(queryModifications, database, additionalFilters, sorts);
	// add to the query 
	query.append(queryModifications.toString());

	qs.setQuery(query.toString());

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
```


## Additional Methods Needed
Copy the following Methods into your class or build your own method to to utilize dynamic query modifications from user input 
```
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
```