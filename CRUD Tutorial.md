# SEMOSS Database CRUD Operations Tutorial
SEMOSS allows quick end to end customization to facilitate create, read, update, and delete operations on a database. In this tutorial, we will be performing CRUD operations on a Movie dataset.

The metamodel for the database is shown below:

![alt text](Backend%20Files/Images/movie-metamodel.PNG)

**Reference the [Movie Management Project](https://repo.semoss.org/semoss-training/backend/project) to follow along with this tutorial.**<br>
*The project folder contains insights to allow the user to perform CRUD operations and the java classes to perform the CRUD operations in the assets/java folder. For simplicity, we have omitted pixel input/output definition. Documentation for grabbing inputs can be referenced in other tutorials.*


## SEMOSS Database Connection
To perform operations on a database in SEMOSS you can get the database by the id. In this example our database is a `RDBMSNativeEngine`. The `RDBMSNativeEngine` provides a method to get a PreparedStatement`getPreparedStatement(String query)`.


## Creating a New Genre
To create a new genre, we first create our sql query to perform the insertion into the table. Then, we get our database connection and create a PreparedStatement from the query. Finally, we add our arguments and execute the PreparedStatment.

```java
	private static String insertQuery = "INSERT INTO GENRE (GENRE, TITLE_FK) VALUES (?, ?)";
    
    RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
    PreparedStatement ps = null;
	try {
		int i = 1;
		ps = database.getPreparedStatement(AddGenreReactor.insertQuery);
		ps.setString(i++, genre);
		ps.setString(i++, title);
		ps.execute();
		database.commit();
	} catch (SQLException e) {
		logger.error(Constants.STACKTRACE, e);
		return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
	} finally {
		ConnectionUtils.closePreparedStatement(ps);
	}

```

**This reactor can be tested in the pixel console in SEMOSS by running:**<br>
```
AddGenre(database=["2555ec1b-e1a2-4905-91e0-022dc57fc564"], title=["Semoss"], genre=["Tech Documentary"]);
```

## Updating a Movie Title
In our Movie Database a Movie Title is the primary key. If we update a Movie Title in one table, we also need to do it in the others. First, we create our sql queries to perform the updates across multiple tables. Then, we get our database connection and create PreparedStatements from the queries. Finally, we add our arguments and execute the PreparedStatements.

```java
	private static final String titleUpdate = "UPDATE TITLE SET TITLE = ? WHERE TITLE = ?";
	private static final String nominatedUpdate = "UPDATE NOMINATED SET TITLE_FK = ? WHERE TITLE_FK = ?";
	private static final String genreUpdate = "UPDATE GENRE SET TITLE_FK = ? WHERE TITLE_FK = ?";
	
	RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
	database.setAutoCommit(false);
	PreparedStatement titlePS = null;
	PreparedStatement genrePS = null;
	PreparedStatement nomintationPS = null;
	try {
		// update title table
		titlePS = database.getPreparedStatement(UpdateMovieReactor.titleUpdate);
		titlePS.setString(1, newTitle);
		titlePS.setString(2, title);
		// update genre table
		genrePS = database.getPreparedStatement(UpdateMovieReactor.genreUpdate);
		genrePS.setString(1, newTitle);
		genrePS.setString(2, title);
		// update nomination table
		nomintationPS = database.getPreparedStatement(UpdateMovieReactor.nominatedUpdate);
		nomintationPS.setString(1, newTitle);
		nomintationPS.setString(2, title);
		titlePS.execute();
		genrePS.execute();
		nomintationPS.execute();
		database.commit();
	} catch (SQLException e) {
		return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
	}
	finally {
		ConnectionUtils.closePreparedStatement(titlePS);
		ConnectionUtils.closePreparedStatement(genrePS);
		ConnectionUtils.closePreparedStatement(nomintationPS);
	}

```

**This reactor can be tested in the pixel console in SEMOSS by running:**<br>
```
UpdateMovie(database=["2555ec1b-e1a2-4905-91e0-022dc57fc564"], title=["Semoss"],  newTitle=["SEMOSS"]);
```

## Deleting a Genre
To perform a deletion on the database, first we write our deletion query. Then, we get our database connection and generate a PreparedStatement. Finally, we add our arguments and execute the PreparedStatement.

```java
	private static String deleteQuery = "DELETE FROM GENRE WHERE GENRE = ?";
    // delete genre from the database
	RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
	PreparedStatement ps = null;
	try {
		int i = 1;
		ps = database.getPreparedStatement(DeleteGenreReactor.deleteQuery);
		ps.setString(i++, genre);
		ps.execute();
	} catch (SQLException e) {
		return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
	} finally {
		ConnectionUtils.closePreparedStatement(ps);
	}
```
**This reactor can be tested in the pixel console in SEMOSS by running:**<br>
```
DeleteGenre(database=["2555ec1b-e1a2-4905-91e0-022dc57fc564"], genre=["Tech Documentary"]);
```
