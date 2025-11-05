---
sidebar_label: 'SelectQueryStruct'
slug: "java/sqs"
description: "How to use SelectQueryStruct"
---

# SelectQueryStruct (SQS) Documentation
 <!-- **Table of Contents:**
 - [Example](#example)
 - [Overview](#overview-of-sqs)
 - [Selectors](#selectors)
 - [Adding an alias](#adding-an-alias)
 - [Filters](#filters)
 - [Joins](#joins)
 - [Grouping](#grouping)
 - [AND Filter](#and-filter)
 - [OR Filter](#or-filter)
 - [Math Functions](#math-functions)
 - [Subquery](#subquery) -->

## Overview Of SQS

The `SelectQueryStruct` (SQS) is a custom object used within the AI Core framework to facilitate querying various sources. It simplifies the process of crafting SQL, R, Python queries by providing a structured approach to define query components such as selectors, filters, sorts, joins, and groupings.

## Example
```java
/** An example that utilizes many different features that the SQS offers **/

SelectQueryStruct qs = new SelectQueryStruct();

// Selectors: Specify columns to retrieve
qs.addSelector(new QueryColumnSelector("EmployeeTable__EmployeeName"));
qs.addSelector(new QueryColumnSelector("EmployeeTable__EmployeeAge"));
qs.addSelector(new QueryColumnSelector("DepartmentTable__DepartmentName"));

// Relations: Implement a left outer join between EmployeeTable and DepartmentTable
qs.addRelation("EmployeeTable__DepartmentID", 
               "DepartmentTable__DepartmentID", "left.outer.join");

// Filters: Combine conditions using AND and OR filters
AndQueryFilter andFilters = new AndQueryFilter();
andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("EmployeeTable__EmployeeAge", ">", "30"));
andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("DepartmentTable__DepartmentName", "==", "Engineering"));

OrQueryFilter orFilters = new OrQueryFilter();
orFilters.addFilter(andFilters);
orFilters.addFilter(SimpleQueryFilter.makeColToValFilter("EmployeeTable__EmployeeName", "=", "John Doe"));

qs.addExplicitFilter(orFilters);

// Math Function: Calculate the average age of employees in each department
QueryFunctionSelector averageAgeFunction = new QueryFunctionSelector();
averageAgeFunction.setFunction(QueryFunctionHelper.AVERAGE_2);
averageAgeFunction.addInnerSelector(new QueryColumnSelector("EmployeeTable__EmployeeAge"));
averageAgeFunction.setAlias("Average_Employee_Age");
qs.addSelector(averageAgeFunction);

// Ordering: Sort results by EmployeeName and DepartmentName
qs.addOrderBy("EmployeeTable__EmployeeName");
qs.addOrderBy("DepartmentTable__DepartmentName");

// Grouping: Group results by DepartmentName
qs.addGroupBy(new QueryColumnSelector("DepartmentTable__DepartmentName"));

// Iterate through the results of the SQS
Iterator<?> iterator = WrapperManager.getInstance().getRawWrapper(database, qs);
while (iterator.hasNext()) {
    // Process each result
}
```
Here is the resultinig SQL Select Statement that is generated:
```sql
SELECT EmployeeTable.EmployeeName, DepartmentTable.DepartmentName, AVG(EmployeeTable.EmployeeAge) AS "AverageAge" 
FROM EmployeeTable LEFT JOIN DepartmentTable ON EmployeeTable.DepartmentID = DepartmentTable.DepartmentID 
WHERE (EmployeeTable.EmployeeAge > 30 
AND DepartmentTable.DepartmentName = 'Engineering') 
OR (Director.Director = 'M._Night_Shyamalan') 
GROUP BY DepartmentTable.DepartmentName, EmployeeTable.EmployeeName 
ORDER BY EmployeeTable.EmployeeName, DepartmentTable__DepartmentName;
```
This example demonstrates how to use the `SelectQueryStruct` to:

-   **Select specific columns**: Retrieve employee *names*, *ages*, and *department names* from the `EmployeeTable` and `DepartmentTable`.
-   **Join tables**: Perform a left outer join between `EmployeeTable` and `DepartmentTable` using `DepartmentID`.
-   **Filter results**: Include employees older than 30 in the Engineering department or named John Doe.
-   **Calculate average**: Compute the average age of employees within each department.
-   **Order results**: Sort the output by employee name and department name.
-   **Group results**: Group the data by department name to aggregate the average age calculation.

## Selectors


Selectors in the `SelectQueryStruct` (SQS) are used to define which columns should be included in the query. They specify the data that will be retrieved from the data source. *QueryColumnSelector* are CASE Sensitive -- Make sure to check the case for the actual table and column to get results.


Here is an example of how selectors are used within an SQS:   

```java  
SelectQueryStruct qs = new SelectQueryStruct();
    
// Adding selectors to specify columns to be retrieved
qs.addSelector(new QueryColumnSelector("USERS" + "__" + "NAME"));
qs.addSelector(new QueryColumnSelector("USERS" + "__" + "AGE"));
qs.addSelector(new QueryColumnSelector("USERS" + "__" + MyConstants.CITY));
```
#### Adding an alias
```java
QueryColumnSelector columnSelector = new QueryColumnSelector("EmployeeTable__EmployeeName"); 
// Set an alias for the column 
columnSelector.setAlias("Employee_Name");
qs.addSelector(columnSelector);
```

-   **`QueryColumnSelector`**: This object represents a column in the database. It takes in the Table and Column Name that you want to retrieve. In this example, *"USERS"* is the Table Name and the Column Name is a value or a const reference.
-   **`addSelector` Method**: This method is used to add a column to the SELECT query. Each call to `addSelector` specifies a new column to be included.
- **`Setting an Alias`**: The `setAlias` method is used to assign a temporary name to the column in the query results. In the example, `columnSelector.setAlias("Employee_Name")` assigns the alias `"Employee_Name"` to the `EmployeeTable__EmployeeName` column.


## Filters

Filters in the `SelectQueryStruct` (SQS) are used to define conditions that must be met for rows to be included in the SELECT query.

Here is an example of how filters are used within an SQS:

```Java
SelectQueryStruct qs = new SelectQueryStruct();

// Adding filters to specify conditions for the query
qs.addExplicitFilter(SimpleQueryFilter.makeColToValFilter("USERS" + "__" + "NAME", "==", "Peter Parker"));
qs.addExplicitFilter(SimpleQueryFilter.makeColToValFilter("USERS" + "__" + VHASuppConst.CITY, "==", "New York City"));` 
```
-   **`SimpleQueryFilter.makeColToValFilter`**: This method creates a filter condition where a column is compared to a specific value. In the example, it specifies that the `NAME` column must be equal to "Peter Parker" and the `CITY` column must be equal to "New York City".
-   **`addExplicitFilter` Method**: This method is used to add a filter condition to the SELECT statement. Each call to `addExplicitFilter` specifies a new condition that must be met for rows to be included in the query results.

### AND Filter

The `AndQueryFilter` allows you to combine multiple conditions using the `AND` keyword, ensuring that all specified criteria are met.

**Example:**

```Java

AndQueryFilter andFilters = new AndQueryFilter();
andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("MovieTable__RottenTomatoes_Audience", ">", 0.8));
andFilters.addFilter(SimpleQueryFilter.makeColToValFilter("MovieTable__RottenTomatoes_Critics", ">", 0.8));` 
```
-   **Purpose**: Filters movies with audience and critic ratings greater than 0.8.

### OR Filter

The `OrQueryFilter` allows you to combine conditions using the `OR` keyword, ensuring that at least one of the specified criteria is met.

**Example:**

```Java

OrQueryFilter orFilters = new OrQueryFilter();
orFilters.addFilter(andFilters);
orFilters.addFilter(SimpleQueryFilter.makeColToValFilter("DirectorTable__DirectorName", "=", "M._Night_Shyamalan"));` 
```
-   **Purpose**: Filters movies directed by M. Night Shyamalan or those with high audience and critic ratings.

### Applying And/Or Filters

To use these filters in your query, add them to the `SelectQueryStruct` using `addExplicitFilter`.

**Example:**

Java

`SelectQueryStruct qs = new SelectQueryStruct();
qs.addExplicitFilter(orFilters);` 

-   **Resulting SQL Statement**:
    
    SQL
    
    `SELECT MovieTitle, DirectorName, RottenTomatoes_Audience, RottenTomatoes_Critics 
    FROM MovieTable 
    LEFT JOIN DirectorTable ON MovieTable.MovieID = DirectorTable.MovieID_FK 
    WHERE DirectorName = 'M._Night_Shyamalan' OR (RottenTomatoes_Audience > 0.8 AND RottenTomatoes_Critics > 0.8);` 
    
-   **Purpose**: Retrieves movies either highly rated by audiences and critics or directed by M. Night Shyamalan.


## Relations (Joins)

Relations in the `SelectQueryStruct` (SQS) are used to implement joins, allowing you to combine data from multiple tables based on related columns. This feature is essential for retrieving comprehensive datasets that span across different tables.

Here is an example of how relations (joins) are used within an SQS:

```Java
SelectQueryStruct qs = new SelectQueryStruct();

// Adding a relation to perform a left outer join between two tables
qs.addRelation("REQUEST_TABLE" + "__" + "ITEM_TYPE", "CATALOG_TABLE" + "__" + "ITEM_TYPE", "left.outer.join");` 
```
-   **`addRelation` Method**: This method is used to define a join between two tables. It specifies the columns to join on and the type of join (e.g., "left.outer.join").

## Ordering

Ordering in the `SelectQueryStruct` (SQS) is used to specify the order in which the query results should be returned. Useful for sorting data.

Here is an example of how ordering is used within an SQS:

```Java
SelectQueryStruct qs = new SelectQueryStruct();

// Adding order by clauses to sort the query results
qs.addOrderBy("REQUEST_TABLE" + "__" + "DATE_CREATED");
qs.addOrderBy("REQUEST_TABLE" + "__" + "REQUEST_ITEM_ID");` 
```
-   **`addOrderBy` Method**: This method is used to add columns to the ORDER BY clause of the query. Each call specifies a new column to sort by.

## Grouping

Grouping in the `SelectQueryStruct` (SQS) is used to group rows that have the same values in specified columns, often used in conjunction with aggregate functions.

Here is an example of how grouping is used within an SQS:

```Java
SelectQueryStruct qs2 = new SelectQueryStruct();

// Adding group by clauses to group the query results
qs2.addGroupBy(new QueryColumnSelector("REQUEST_ITEM_ID"));
qs2.addGroupBy(new QueryColumnSelector("PICK_TICKET_STATUS"));` 
```
-   **`addGroupBy` Method**: This method is used to add columns to the GROUP BY clause of the query. Each call specifies a new column to group by.
-   **`QueryColumnSelector`**: Represents the column to be used for grouping.

## Limit/Offset

To limit the amount of data being returned you can add a limit and offset

```java
qs.setLimit(100);

qs.setOffSet(20);
```

## Math Functions
This tutorial demonstrates how to incorporate mathematical functions such as `SUM`, `COUNT`, and `AVG` into `SelectQueryStructs` using the Movies Database and `MathFunctionsReactor.java`.

### Adding Math Functions (AVG, SUM, and COUNT)
```Java
SelectQueryStruct qs = new SelectQueryStruct(); 

qs.addSelector(new QueryColumnSelector("Director__Director"));

// Add AVG function to SQS
QueryFunctionSelector averageFunction = new QueryFunctionSelector();
averageFunction.setFunction(QueryFunctionHelper.AVERAGE_2);
averageFunction.addInnerSelector(new QueryColumnSelector("Title__RottenTomatoes_Critics"));
averageFunction.setAlias("Average_Critic_Rating");
// Add the math function to the SQS as a selector
qs.addSelector(averageFunction);

// Add SUM function to SQS
QueryFunctionSelector sumFunction = new QueryFunctionSelector();
sumFunction.setFunction(QueryFunctionHelper.SUM);
sumFunction.addInnerSelector(new QueryColumnSelector("Title__Revenue_Domestic"));
sumFunction.setAlias("Total_Domestic_Revenue");
// Add the math function to the SQS as a selector
qs.addSelector(sumFunction);

// Add COUNT function to SQS
QueryFunctionSelector countFunction = new QueryFunctionSelector();
countFunction.setFunction(QueryFunctionHelper.COUNT);
countFunction.addInnerSelector(new QueryColumnSelector("Title__Title"));
countFunction.setAlias("Total_Movies_Directed");
// Add the math function to the SQS as a selector
qs.addSelector(countFunction);
```
### Resulting SQL Statement

The constructed query will be:

```SQL
SELECT DISTINCT 
    Director.Director AS "Director", 
    AVG(Title.RottenTomatoes_Critics) AS "Average_Critic_Rating", 
    SUM(Title.Revenue_Domestic) AS "Total_Domestic_Revenue", 
    COUNT(Title.Title) AS "Total_Movies_Directed" 
FROM Title 
LEFT JOIN Director ON Title.Title = Director.Title_FK 
GROUP BY Director.Director;
```
This query retrieves each director's name, the average critic rating of their films, the total domestic revenue, and the total number of films directed. For more details on table joins, refer to the relations tutorial.


## Subquery
This section demonstrates the basics of creating and adding a subquery to the Select Query Struct. Oftentimes these subqueries are broken off into a separate method that returns a SelectQueryStruct object that can be joined to the main SQS  via the `addRelation(new SubqueryRelationship)` method.

### Simple Example

```Java
// Main query setup
SelectQueryStruct mainQuery = new SelectQueryStruct();
mainQuery.addSelector(new QueryColumnSelector("Catalog__CanRequest"));
mainQuery.addSelector(new QueryColumnSelector("Catalog__CanShip"));

// Subquery setup
SelectQueryStruct subQuery = new SelectQueryStruct();
subQuery.addSelector(new QueryColumnSelector("PickTicket__RequestItemID"));
subQuery.addSelector(new QueryColumnSelector("PickTicket__Status"));

// Link subquery to main query
List<String[]> joinDetails = new ArrayList<>();
joinDetails.add(new String[] {"Request__RequestItemID", "PickTicket__RequestItemID", "="});
mainQuery.addRelation(new SubqueryRelationship(subQuery, "PickTicket", "left.join", joinDetails));` 
```

-   **Main Query**: The primary query that retrieves data from the main tables.
-   **SubQuery**: A secondary query that retrieves related data, which can be used to enhance or filter the main query's results.
-   **Linking**: The subquery is linked to the main query using `SubqueryRelationship`, specifying how the subquery results should be joined with the main query.