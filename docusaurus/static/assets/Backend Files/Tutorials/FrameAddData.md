# How to add data to a frame with a Pixel command
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [FrameCreateAndAddDataReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/FrameCreateAndAddDataReactor.java) to follow along with this tutorial.**<br>

## Add Reactor Keys
`ReactorKeysEnum.DATABASE.getKey()` is the only mandatory reactor key for this example. You can add custom reactor keys to this constructor should you need other query modifying keys such as, Filters, Sort, Limit and Offset.

```
	public FrameCreateAndAddDataReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey() };
	}
```

##  Query database
Overriding the IReactor's `execute()` method, add the following:
```
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);

		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		String query = "SELECT TITLE, MOVIEBUDGET, REVENUE_DOMESTIC " 
				+ "FROM TITLE "
				+ "WHERE MOVIEBUDGET > 20000 ";
```

## Set up the frame
In order to create a frame you have to specify the headers and types metadata. In this example we show how to acheive this using a H2 frame and the `ImportUtility`.
```
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		// set up frame data
		String[] headers = new String[] { "TITLE", "MOVIEBUDGET", "REVENUE_DOMESTIC" };
		String[] types = new String[] { "STRING", "NUMBER", "NUMBER" };
		H2Frame frame = new H2Frame("MoviesFrame");
		ImportUtility.parseHeadersAndTypeIntoMeta(frame, headers, types, frame.getName());
		frame.getBuilder().alterTableNewColumns(frame.getName(), headers, types);
		frame.syncHeaders();

		// create prepared statement to insert data into frame
		PreparedStatement insertPS = frame.createInsertPreparedStatement(headers);
```

## Getting the data into the frame
In this step we query the database and loop our data from the `ResultSet` into the H2 frame
```
			conn = database.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			// grab values and insert into frame
			
			while (rs.next()) {
				String title = rs.getString(1);
				float budget = rs.getFloat(2);
				float revenue = rs.getFloat(3);
				insertPS.setString(1, title);
				insertPS.setFloat(2, budget);
				insertPS.setFloat(3, revenue);
				insertPS.addBatch();
			}
			insertPS.executeBatch();
```

## Sync data and return the Frame
Lastly, we add the frame to the insight using `setDataMaker` and return the NounMetadata.
```
		this.insight.setDataMaker(frame);
		NounMetadata retNoun = new NounMetadata(frame, PixelDataType.FRAME, PixelOperationType.FRAME, PixelOperationType.FRAME_HEADERS_CHANGE, PixelOperationType.FRAME_DATA_CHANGE);
		return retNoun;
```
Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for more examples.