package reactors;

import java.util.Arrays;

import prerna.ds.OwlTemporalEngineMeta;
import prerna.ds.r.RDataTable;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.PixelOperationType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.AddHeaderNounMetadata;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.frame.r.AbstractRFrameReactor;

public class RScriptReactor extends AbstractRFrameReactor{

	/**
	 * This reactor duplicates and existing column and adds it to the frame. The
	 * inputs to the reactor are: 1) the name for the column to duplicate 2) the new
	 * column name
	 * 
	 * To test this reactor you need a database with the movie data. You will need
	 * to add data to an R frame and pass this frame into this reactor which can be tested
	 * in the pixel console in SEMOSS by running: 
	 * MoviesFrame | RScript ( database = [ "<databaseId>" ], column = ["<columnName>"], newCol = ["<newColumnName>"] ) ;
	 */

	public RScriptReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), ReactorKeysEnum.COLUMN.getKey(),
				ReactorKeysEnum.NEW_COLUMN.getKey() };
	}

	@Override
	public NounMetadata execute() {
		init();
		organizeKeys();

		String databaseId = this.keyValue.get(this.keysToGet[0]);
		
		// get frame
		RDataTable frame = (RDataTable) getFrame();
		String table = frame.getName();

		// get source column to duplicate
		String srcCol = this.keyValue.get(this.keysToGet[1]);

		// make sure source column exists
		String[] allCol = getColumns(table);
		if (srcCol == null || !Arrays.asList(allCol).contains(srcCol)) {
			throw new IllegalArgumentException("Need to define an existing column to duplicate.");
		}

		// clean and validate new column name or use default name
		String newColName = getCleanNewColName(frame, srcCol + "_DUPLICATE");
		String inputColName = this.keyValue.get(this.keysToGet[2]);
		if (inputColName != null && !inputColName.isEmpty()) {
			inputColName = getCleanNewColName(frame, inputColName);
			// entire new name could be invalid characters
			if (!inputColName.equals("")) {
				newColName = inputColName;
			}
		}

		// run duplicate script
		String duplicate = table + "$" + newColName + "<-" + table + "$" + srcCol + ";";
		frame.executeRScript(duplicate);
		this.addExecutedCode(duplicate);

		// get src column data type
		OwlTemporalEngineMeta metaData = frame.getMetaData();
		String dataType = metaData.getHeaderTypeAsString(table + "__" + srcCol);
		if(dataType == null)
			return getWarning("Frame is out of sync / No Such Column. Cannot perform this operation");

		String adtlDataType = metaData.getHeaderAdtlType(frame.getName() + "__" + srcCol);

		// update meta data
		metaData.addProperty(table, table + "__" + newColName);
		metaData.setAliasToProperty(table + "__" + newColName, newColName);
		metaData.setDataTypeToProperty(table + "__" + newColName, dataType);
		if(adtlDataType != null && !adtlDataType.isEmpty()) {
			metaData.setAddtlDataTypeToProperty(frame.getName() + "__" + newColName, adtlDataType);
		}
		
		NounMetadata retNoun = new NounMetadata(frame, PixelDataType.FRAME, PixelOperationType.FRAME_HEADERS_CHANGE, PixelOperationType.FRAME_DATA_CHANGE);
		retNoun.addAdditionalReturn(new AddHeaderNounMetadata(newColName));
		return retNoun;
	}
}