# Adding Math Functions to Queries 
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [MathFunctionsReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/MathFunctionsReactor.java) to follow along with this tutorial.**<br>


This tutorial will go over adding math function (Sum, Count, Avg) to SelectQueryStructs


## We begin by creating a new SQS
```
	SelectQueryStruct qs = new SelectQueryStruct();
```

## Adding Selectors
Let Specify columns 

```
	qs.addSelector(new QueryColumnSelector("Director__Director"));
```
We want to query the database for the Average Rotten Tomatoe Critic Score for every director. 

## Creating a Function to use average
We begin by defining a new `QueryFunctionSelector`
```
	QueryFunctionSelector averageFunction = new QueryFunctionSelector();
```
Next we to set the function that we want the averageFunction to use 
```
	averageFunction.setFunction(QueryFunctionHelper.AVERAGE_2);
```
The QueryFunctionHelper helper has a preset constants you can use. Average_2 is just the constant for AVG
Next we set the column that we want the average for and set the alias for that column. 
```
	averageFunction.addInnerSelector(new QueryColumnSelector("Title__RottenTomatoes_Critics"));
	averageFunction.setAlias("Average_Critic_Rating");
```
The last step is to set this function into our main SelectQueryStruct 
```
	qs.addSelector(averageFunction);
```
This sets our current query to be Select Director.Director, Avg(Title.RottenTomatoes_Critics)
Disclaimer, this file does a join on the title table. Please head over to the relations tutorials page if you need a refesher on it. 

### Adding more Math Functions 
You can add multiple functions using this method. So lets add functions to get the total number of films a director has directed and the sum of the domestic revenue for each director. 
```
	QueryFunctionSelector sumFunction = new QueryFunctionSelector();
	sumFunction.setFunction(QueryFunctionHelper.SUM);
	sumFunction.addInnerSelector(new QueryColumnSelector("Title__Revenue_Domestic"));
	sumFunction.setAlias("Total_Domestic_Revenue");
	qs.addSelector(sumFunction);
		
	QueryFunctionSelector countFunction = new QueryFunctionSelector();
	countFunction.setFunction(QueryFunctionHelper.COUNT);
	countFunction.addInnerSelector(new QueryColumnSelector("Title__Title"));
	countFunction.setAlias("Total_Movies_Directed");
	qs.addSelector(countFunction);
```

Doing all the above leaves with us with a sql statment of - 
SELECT DISTINCT Director.Director AS "Director" , AVG(Title.RottenTomatoes_Critics) AS "Average_Critic_Rating" , SUM(Title.Revenue_Domestic) AS "Total_Domestic_Revenue" , COUNT(Title.Title) AS "Total_Movies_Directed" FROM Title Title left join Director Director on Title.Title=Director.Title_FK GROUP BY Director.Director

