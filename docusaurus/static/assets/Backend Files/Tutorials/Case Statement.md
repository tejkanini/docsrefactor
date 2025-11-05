# Case Statement Selector
In this tutorial we will create a query to generate a "Movie Recommendation" column based on some conditions.

**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [CaseStatementQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/CaseStatementQueryReactor.java) to follow along with this tutorial.**<br>


## QueryIfSelector
We will use the `QueryIfSelector.makeQueryIfSelector()` for simplicity, this method takes in the condition, prescedent, antecedent and alias. Our Movie Recommendation column will be based on RottenTomatoes_Audience

<br>CASE<br>
when RottenTomatoes_Audience >= .8 then High <br>
when RottenTomatoes_Audience >= .7 then Medium <br>
when RottenTomatoes_Audience >= .5 then Low <br>
ELSE ""
as "Movie Recommendation"



```
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
```