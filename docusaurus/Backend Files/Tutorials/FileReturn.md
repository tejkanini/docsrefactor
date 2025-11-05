# How to return a file in Pixel?
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [ExportToExcelReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/ExportToExcelReactor.java) to follow along with this tutorial.**<br>

In this tutorial, we will demonstrate how to return an Excel (xlsx) file from a Reactor.

## Specify Query Variable and Add Reactor Keys
In our first step we will specify our base query as a `public static String mainQuery` variable. 
`ReactorKeysEnum.DATABASE.getKey()` is the only mandatory reactor key for this example. You can add custom reactor keys to this constructor should you need other query modifying keys such as, Filters, Sort, Limit and Offset.

```
	public static String mainQuery = "SELECT TITLE, MOVIEBUDGET, REVENUE_DOMESTIC " 
					+ "FROM TITLE "
					+ "WHERE MOVIEBUDGET > 20000 ";

	public ExportToExcelReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), ReactorKeysEnum.FILTERS.getKey(),
				ReactorKeysEnum.SORT.getKey(), ReactorKeysEnum.LIMIT.getKey(), ReactorKeysEnum.OFFSET.getKey() };
	}
```

##  Setting up the Query and Data Source
Overriding the IReactor's `execute()` method, add the following:
```
        organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		// get the database source
		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		
		StringBuilder query = new StringBuilder();
		query.append(mainQuery);
```

##  Add Query Modifications (Optional)
If you need to make use of query modifiers such as Filters, Sort, Limit and Offset you can add the following code:
```
StringBuilder queryModifications = new StringBuilder();
		GenRowFilters additionalFilters = getFilters();
		List<IQuerySort> additionalSort = getSort();

		appendQueryModifications(queryModifications, database, additionalFilters, null, false);
		if (!queryModifications.toString().isEmpty()) {
			query.append("WHERE " + queryModifications.toString());
		}
		queryModifications = new StringBuilder();
		appendQueryModifications(queryModifications, database, null, additionalSort);
		query.append(queryModifications.toString());

		// limit/offset
		queryModifications = new StringBuilder();
		appendQueryModifications(queryModifications, database, getLimit(), getOffset());
		query.append(queryModifications.toString());
```

##  Execute Query on DB
This follows the same process as the `HardSelectQueryStruct` as referenced in [HardQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/query/HardQueryReactor.java) and [HardSelectQueryStruct.md](https://repo.semoss.org/semoss-training/backend/-/blob/master/tutorials/HardSelectQueryStruct.md).

```
		HardSelectQueryStruct qs = new HardSelectQueryStruct();
		qs.setQuery(query.toString());
		IRawSelectWrapper iterator;
		try {
			iterator = WrapperManager.getInstance().getRawWrapper(database, qs);
			if (!iterator.hasNext()) {
				throw new IllegalArgumentException("No results");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String error = "Error occurred running the query to get the results";
			if (e.getMessage() != null && !e.getMessage().isEmpty()) {
				error += ": " + e.getMessage();
			}
			throw new IllegalArgumentException(error);
		}
```

##  Get the Results and send to Excel File.
A few preliminary steps are required before writing to the file. First we need generate or get a file name. Secondly, we need to specify the directory this file will be downloaded to. If the file or the directory does not exist we will create them. Lastly, after setting the file key and file path to the `insightFile`, we call the insight's `addExportFile` and then return the `NounMetadata`.
```
this.task = new BasicIteratorTask(qs, iterator);

		NounMetadata retNoun = null;

		// is data filtered check
		boolean isFiltered = additionalFilters != null && !additionalFilters.isEmpty();

		// get a random file name
		String exportName = getExportFileName("My Movies Details", "xlsx", isFiltered);

		// grab file path to write the file
		this.fileLocation = this.keyValue.get(ReactorKeysEnum.FILE_PATH.getKey());

		// if the file location is not defined generate a random path and set
		// location so that the front end will download
		if (this.fileLocation == null) {
			String insightFolder = this.insight.getInsightFolder();
			{
				File f = new File(insightFolder);
				if (!f.exists()) {
					f.mkdirs();
				}
			}
			this.fileLocation = insightFolder + DIR_SEPARATOR + exportName;

			// store it in the insight so the FE can download it
			// only from the given insight
			String downloadKey = UUID.randomUUID().toString();
			InsightFile insightFile = new InsightFile();
			insightFile.setDeleteOnInsightClose(true);
			insightFile.setFinsightFileileKey(downloadKey);
			insightFile.setFilePath(this.fileLocation);
			this.insight.addExportFile(downloadKey, insightFile);
			retNoun = new NounMetadata(downloadKey, PixelDataType.CONST_STRING, PixelOperationType.FILE_DOWNLOAD);

		} else {
			retNoun = new NounMetadata(this.fileLocation, PixelDataType.CONST_STRING);
		}
		buildTask();
		retNoun.addAdditionalReturn(NounMetadata.getSuccessNounMessage("Successfully generated the excel file"));
		return retNoun;
```

## Additional Utility Methods needed
```
	protected String fileLocation;
	protected ITask task;
	
	protected void buildTask() {
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
		CreationHelper createHelper = workbook.getCreationHelper();
		SXSSFSheet sheet = workbook.createSheet("Results");
		sheet.setRandomAccessWindowSize(100);
		// freeze the first row
		sheet.createFreezePane(0, 2);

		int colIndex = 0;
		int size = 0;
		// create typesArr as an array for faster searching
		String[] headers = null;
		SemossDataType[] typesArr = null;

		// style dates
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
		// style timestamps
		CellStyle timeStampCellStyle = workbook.createCellStyle();
		timeStampCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));

		// the excel data row
		Row excelRow = null;
		int excelRowCounter = 0;

		// we need to iterate and write the headers during the first time
		if(this.task.hasNext()) {
			IHeadersDataRow row = this.task.next();
			List<Map<String, Object>> headerInfo = this.task.getHeaderInfo();
			
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			
			// export the time of the report generation
			{
				// create a CellStyle with the font
				CellStyle timestampStyle = workbook.createCellStyle();
				timestampStyle.setFont(headerFont);
				timestampStyle.setAlignment(HorizontalAlignment.LEFT);
				timestampStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				Row timestampRow = sheet.createRow(excelRowCounter);
				// create a Font for styling header cells
				Cell cell = timestampRow.createCell(0);
				cell.setCellValue("Reported generated on: " + new SemossDate(LocalDateTime.now()).getFormatted("yyyy-MM-dd HH:mm:ss"));
				cell.setCellStyle(timestampStyle);
			}
						
			// create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
			headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// generate the header row
			Row headerRow = sheet.createRow(++excelRowCounter);
			// and define constants used throughout like size, and types
			colIndex = 0;
			headers = row.getHeaders();
			size = headers.length;
			typesArr = new SemossDataType[size];
			for(; colIndex < size; colIndex++) {
				Cell cell = headerRow.createCell(colIndex);
				cell.setCellValue(headers[colIndex]);
				cell.setCellStyle(headerCellStyle);
				typesArr[colIndex] = SemossDataType.convertStringToDataType(headerInfo.get(colIndex).get("type") + "");
			}

			// generate the data row
			excelRow = sheet.createRow(++excelRowCounter);
			Object[] dataRow = row.getValues();
			colIndex = 0;
			for(; colIndex < size; colIndex ++) {
				Cell cell = excelRow.createCell(colIndex);
				Object value = dataRow[colIndex];
				if(value == null) {
					cell.setCellValue("");
				} else {
					if(typesArr[colIndex] == SemossDataType.STRING) {
						cell.setCellValue(value + "");
					} else if(typesArr[colIndex] == SemossDataType.INT || typesArr[colIndex] == SemossDataType.DOUBLE) {
						cell.setCellValue( ((Number) value).doubleValue() ) ;
					} else if(typesArr[colIndex] == SemossDataType.DATE) {
						cell.setCellValue( ((SemossDate) value).getDate() ) ;
						cell.setCellStyle(dateCellStyle);
					} else if(typesArr[colIndex] == SemossDataType.TIMESTAMP) {
						cell.setCellValue( ((SemossDate) value).getDate() ) ;
						cell.setCellStyle(timeStampCellStyle);
					} else if(typesArr[colIndex] == SemossDataType.BOOLEAN) {
						cell.setCellValue( (boolean) value);
					} else {
						cell.setCellValue(value + "");
					}
				}
			}
		}

		// now iterate through all the data
		while(this.task.hasNext()) {
			excelRow = sheet.createRow(++excelRowCounter);
			IHeadersDataRow row = this.task.next();
			Object[] dataRow = row.getValues();
			colIndex = 0;
			for(; colIndex < size; colIndex ++) {
				Cell cell = excelRow.createCell(colIndex);
				Object value = dataRow[colIndex];
				if(value == null) {
					cell.setCellValue("");
				} else {
					if(typesArr[colIndex] == SemossDataType.STRING) {
						cell.setCellValue(value + "");
					} else if(typesArr[colIndex] == SemossDataType.INT || typesArr[colIndex] == SemossDataType.DOUBLE) {
						cell.setCellValue( ((Number) value).doubleValue() ) ;
					} else if(typesArr[colIndex] == SemossDataType.DATE) {
						cell.setCellValue( ((SemossDate) value).getDate() ) ;
						cell.setCellStyle(dateCellStyle);
					} else if(typesArr[colIndex] == SemossDataType.TIMESTAMP) {
						cell.setCellValue( ((SemossDate) value).getDate() ) ;
						cell.setCellStyle(timeStampCellStyle);
					} else if(typesArr[colIndex] == SemossDataType.BOOLEAN) {
						cell.setCellValue( (boolean) value);
					} else {
						cell.setCellValue(value + "");
					}
				}
			}
		}
		
		// fixed size at the end
		colIndex = 0;
		for(; colIndex < size; colIndex++) {
			sheet.setColumnWidth(colIndex, 5_000);
		}

		String password = this.keyValue.get(ReactorKeysEnum.PASSWORD.getKey());
		if(password != null) {
			// encrypt file
			ExcelUtility.encrypt(workbook, this.fileLocation, password);
		} else {
			// write file
			ExcelUtility.writeToFile(workbook, this.fileLocation);
		}
	}

	protected void appendQueryModifications(StringBuilder query, IEngine app, GenRowFilters additionalFilters, List<IQuerySort> additionalSort) {
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
	
	/**
	 * Getting a file name
	 * @param extension
	 * @return
	 */
	protected String getExportFileName(String fileNamePrefix, String extension, boolean hasFilters) {
		// get a random file name
		SemossDate sDate = new SemossDate(LocalDateTime.now());
		String dateFormatted = sDate.getFormatted("yyyy-MM-dd HH-mm-ss");
		String exportName = null;
		if (hasFilters) {
			exportName = fileNamePrefix + " (Filtered) - " + dateFormatted + "." + extension;
		} else {
			exportName = fileNamePrefix + " - " + dateFormatted + "." + extension;
		}
		return exportName;
	}
	
	protected GenRowFilters getFilters() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.FILTERS.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata filterNoun = inputsGRS.getNoun(0);
			SelectQueryStruct qs = (SelectQueryStruct) filterNoun.getValue();
			GenRowFilters filters = qs.getCombinedFilters();
			return filters;
		}
		return null;
	}
	
	protected List<IQuerySort> getSort() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.SORT.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata sortNoun = inputsGRS.getNoun(0);
			SelectQueryStruct qs = (SelectQueryStruct) sortNoun.getValue();
			List<IQuerySort> orderBy = qs.getOrderBy();
			return orderBy;
		} 
		return null;
	}
	
	protected int getLimit() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.LIMIT.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata limitNoun = inputsGRS.getNoun(0);
			return ((Number) limitNoun.getValue()).intValue();
		}
		return -1;
	}
	
	protected int getOffset() {
		GenRowStruct inputsGRS = this.store.getNoun(ReactorKeysEnum.OFFSET.getKey());
		if (inputsGRS != null && !inputsGRS.isEmpty()) {
			NounMetadata offsetNoun = inputsGRS.getNoun(0);
			return ((Number) offsetNoun.getValue()).intValue();
		}
		return -1;
	}
	
	protected void appendQueryModifications(StringBuilder query, IEngine app, int limit, int offset) {
		AbstractSqlQueryUtil queryUtil = ((RDBMSNativeEngine) app).getQueryUtil();
		queryUtil.addLimitOffsetToQuery(query, limit, offset);
	}
```
<br>
Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for additional examples.