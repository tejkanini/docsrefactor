# SelectQueryStruct
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [SimpleQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/SimpleQueryReactor.java) to follow along with this tutorial.**<br>
To query a database in SEMOSS use the SelectQueryStruct object. The SelectQueryStruct holds all the information for the query such as selectors, filters, joins, order by, etc.

```
	SelectQueryStruct qs = new SelectQueryStruct();
```


## Adding Selectors
To specify which column you want to select, you need to add your column written as "TABLENAME__COLUMN". In the movies dataset to select the column Movie_Budget from the Title table we would have to add it in as "Title__Movie_Budget"

QUERYCOLUMNSELECTOR are CASE Sensitive -- Make sure to check the case for the actual table and column to get results. 

```
	qs.addSelector(new QueryColumnSelector("Title__Title"));
	qs.addSelector(new QueryColumnSelector("Title__Movie_Budget"));
	qs.addSelector(new QueryColumnSelector("Title__Revenue_Domestic"));
```

## Adding Filters
You can add filters by the `addExplicitFilter` method. In this example we are only selecting titles that have a Movie_Budget > 2000. This filter will get added everytime the code gets executed.
```
	qs.addExplicitFilter(SimpleQueryFilter.makeColToValFilter("Title__Movie_Budget", ">", 20000));

```

You can add dynamic filters passed in the pixel by grabbing the filter input and adding them to the query struct.
```
	GenRowFilters additionalFilters = getFilters();
	if (additionalFilters != null) {
		qs.mergeExplicitFilters(additionalFilters);
	}
```

## Adding Sort
You can also add a sort by grabbing the sort input and adding it to the query struct.
```
	// add sort option passed in
	List<IQuerySort> sorts = getSort();
	if (sorts != null) {
		qs.setOrderBy(sorts);
	}
```

## Adding Limit/Offset
To limit the amount of data being returned you can add a limit and offset
```
	qs.setLimit(getLimit());
	qs.setOffSet(getOffset());

```

## Getting the Results
Finally once the query structure is created, we can execute the query and return the results. The query is executed on the database and an iterator is provided. Semoss provides a `BasicIteratorTask` to create the task payload.

```
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