# Adding Query Functions to SelectQueryStruct Queries 
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [QueryFunctionReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/QueryFunctionReactor.java) to follow along with this tutorial.**<br>


This tutorial will go over adding single query functions like (Count) and how to daisy chain two or more query functions (Group_concat, Coalesce) on the same column in SelectQueryStructs


## We begin by creating a new SQS
```
	SelectQueryStruct qs = new SelectQueryStruct();
```

## Adding Selectors
Let Specify columns 

```
	qs.addSelector(new QueryColumnSelector("TITLE__TITLE"));
```
We want to query the database for the Counts of Movie Titles. 

## Creating a Function to use Count()
We begin by defining a new `QueryFunctionSelector`
```
	QueryFunctionSelector selectFun = new QueryFunctionSelector();
```
Next we to set the function that we want the averageFunction to use 
```
	selectFun.setFunction(QueryFunctionHelper.COUNT);
```
The QueryFunctionHelper helper has a preset constants you can use. Count is just the constant for COUNT()
Next we set the column that we want the count for and set the alias for that column. 
```
	selectFun.setAlias("Counts");
	selectFun.addInnerSelector(new QueryColumnSelector("TITLE__TITLE"));
```
The last step is to set this function into our main SelectQueryStruct 
```
	qs.addSelector(selectFun);
```
This sets our current query to be Select Title.Title, Count(Title.Title)

Additionally, if we use a function like count() and the SQL dialect of the database we are using requires us to include it in a GroupBy(), we must include the following line in our code
```
	qs.addGroupBy(new QueryColumnSelector("TITLE__TITLE"));
```

### Adding more Query Functions 
You can add multiple functions using this method. So lets add functions to get the total number of films a director has directed and the sum of the domestic revenue for each director. 
```
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
```

Doing all the above leaves with us with a sql statement of - 
```
SELECT
	DISTINCT TITLE.TITLE AS "TITLE",
	Count(TITLE.TITLE) AS "Counts",
	GROUP_CONCAT(COALESCE(TITLE.ROTTENTOMATOES_CRITICS,'')) AS "Critics Score"
FROM
	TITLE
	GROUP BY TITLE.TITLE
```