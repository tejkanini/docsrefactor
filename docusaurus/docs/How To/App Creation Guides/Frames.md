<!-- TODO need a blurb about what a frame is-->

<!-- TODO need steps on how to create a frame in a notebook-->

### Creating Frames in the UI
You can load data from various sources into frames

1. Go to AI Core UI
2. Click the Create New Insight (+) button
3. Upload a new File
4. Click the gear icon located at the bottom left of the panel.
5. Choose the IDataFrame type you want to use
Always choose the appropriate frameType:
    1. GRID for tabular data
    2. PY for Python
    3. R for R scripts
    4. GRAPH for visualizations
6. Import data!

### Changing Frame Types
You can update your dashboards, notebooks and change the frame type via pixel

Depending on the context of your frame, you would need to change the Pixel step that determines the frame type

In this example following the steps from the UI above
1. Open the terminal
2. Copy the all the pixels commands and change the frame type:
```
FileRead ( filePath = [ "/your_file_name.xlsx" ] , dataTypeMap = [ { "INT" , "Authorized_Supplier" : "STRING" , "Authorized_Supplier_Product_Number" : "INT" , "UOP" : "STRING" , "Qty_Ordered" : "INT" , "Quantity_UoP_Back_Order" : "INT" , "Backorder_Date" : "DATE" , "Expected_BO_Release_Date" : "DATE" , "FY_YTD_Qty_Ordered" : "INT" , "Prior_FY_Qty_Ordered" : "INT" , "FY_Backorder_Qty" : "INT" , "Customers_on_Backorder" : "INT" , "Item_on_Allocation" : "STRING" , "Problem_Cause" : "STRING" , "Comments" : "STRING" } ] , delimiter = [ "," ] , newHeaders = [ { } ] , sheetName = [ "Backorder Report" ] , sheetRange = [ "A1:Y2" ] , fileName = [ "backorder_test_upload.xlsx" ] , additionalDataTypes = [ ...] ) | Import ( frame = [ CreateFrame ( frameType = [ GRID ] , override = [ true ] ) .as ( [ "your_file_name_xlsx_FRAME845276" ] ) ] ) ;

output: {"name": "your_file_name_xlsx_FRAME845276", "type": "GRID"}
```


4. Paste and execute all the pixels to have the data reload in the new frame!

### Frame Pixels
Here are some helpful pixels for frames

#### Creating a new Frame
The pixel to create a new frame
```
CreateFrame ( frameType = [ GRID ] , override = [ true ] ) .as ( [ "frameName" ] ) ;
```
#### View Exisiting columns in a Frame
```
frameName | FrameHeaders();
```

#### Frame Type
To check the type of frame you are using
```
frameName | FrameType();
```