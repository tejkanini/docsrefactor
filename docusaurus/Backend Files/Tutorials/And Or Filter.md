# Adding AND/OR Filters
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [AndOrQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/AndOrQueryReactor.java) to follow along with this tutorial.**<br>


This tutorial goes over using the AndQueryFilter and OrQueryFilter to add more complex filter to your SelectQueryStructs. 
The purpose of this tutorial is to query the Movie DB to find movies that A. Have a high Audience and Critics Score or 
B. Are Directed by the great M Night Shyamalan 


## We begin by creating a new SQS
```
	SelectQueryStruct qs = new SelectQueryStruct();
```


## Adding Selectors
To specify which column you want to select, you need to add your columns. Again at any point you need a reminder of the syntax head over to the [SimpleQueryReactor.md](https://repo.semoss.org/semoss-training/backend/-/blob/master/\tutorials\SelectQueryStruct.md) 

```
	qs.addSelector(new QueryColumnSelector("TITLE__Title"));
	qs.addSelector(new QueryColumnSelector("Director__Director"));
	qs.addSelector(new QueryColumnSelector("TITLE__RottenTomatoes_Audience"));
	qs.addSelector(new QueryColumnSelector("TITLE__RottenTomatoes_Critics"));
```

## And Filter
You can add mulitple filter by using the `addExplicitFilter` which will be combined by the `AND` keyword. For the purpose of this tutorial we will be using the `AndQueryFilter`. 
```
	AndQueryFilter andFilters = new AndQueryFilter();
```
Next add the filter that will be combined to add the SQS later. 
```
	andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("TITLE__RottenTomatoes_Audience", ">", 0.8));
	andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("TITLE__RottenTomatoes_Critics", ">", 0.8));
```
The andFilters will be (RottenTomatoes_Audience > .8 and RottenTomatoes_Critics > .8)

## Or Filter
We also want to have a Or filter for the director. To do this we will create a `OrQueryFilter`
```
	OrQueryFilter orFilters = new OrQueryFilter();
```
We can add a prexisting filter to this object. 
```
	orFilters.addFilter(andFilters);
```
And any subsequent filter added in will be seperated by the `OR` keyword
```
	orFilters.addFilter(SimpleQueryFilter.makeColToValFilter("Director__Director", "=", "M._Night_Shyamalan"));
```
The variable orFilters contains filters that equate the following - Director = 'M._Night_Shyamalan' or (RottenTomatoes_Audience > .8 and RottenTomatoes_Critics > .8);
For these filters to be used, we must add them to our base SelectQueryStruct `sq` by using the `addExplicitFilter` 
```
	qs.addExplicitFilter(orFilters);
```
Disclaimer for the purpose of this tutorial we have joined the Title Table with the Director Table. Adding relations is reviewed in a different page. 

Doing all the above leaves with us with a sql statment of - 
Select Title, Director, RottenTomatoes_Audience, RottenTomatoes_Critics from Title left join Director on Title.Title = Director.Title_FK where Director = 'M._Night_Shyamalan' or (RottenTomatoes_Audience > .8 and RottenTomatoes_Critics > .8);

This query returns all movies that the Critics/Audience have rated higher than an 80 or movies directed by M Night. All make for a good movie night! Grab the popcorn!
