# Adding Relations
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [RelationQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/RelationQueryReactor.java) to follow along with this tutorial.**<br>


This tutorial goes over joining and adding relations to a Select Query Struct 


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
The selectors we have choosen above include one that is part of a different table. For our query to succesfully run, we need to make sure we Join the tables that are required for our projections. 

## Adding a Relation 
You can add relations to a SQS by using the `addRelation` function. Which is part of the selectQueryStruct class. 
```
	qs.addRelation(fromConcept, toConcept, joinType, comparator, relName);
    qs.addRelation(fromConcept, toConcept, joinType);
```
For this example, we need to join the Director and Title table based on the movie titles.  
```
	qs.addRelation("TITLE__Title", "Director__Title_FK", "left.join");
```
This adds a left join relation between the Title and Director table of our database. Matching on the Title and Title FK. Title__Title being the from concept and the Director__Title_FK being the toConcept. "left.join" being the joinType. 

A `qs` can have multiple different joins in a statment. Lets add another one to our query. 
```
	qs.addSelector(new QueryColumnSelector("Genre__Genre"));
```
We've added another selector to our query, which will also need another join. This time on the Genre table. 
```
	qs.addRelation("TITLE__Title", "Genre__Title_FK", "left.join");
```
Doing all the above leaves with us with a sql statment of - 
Select Title, Director, RottenTomatoes_Audience, RottenTomatoes_Critics, Genre from Title left join Director on Title.Title = Director.Title_FK left join Genre on Title.Title = Genre.Title_FK

This query returns the Title, Director, Genre, RottenTomatoes audience and critics reviews for all the movies. 
