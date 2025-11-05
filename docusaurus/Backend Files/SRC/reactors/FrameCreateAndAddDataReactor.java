package reactors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import prerna.ds.rdbms.h2.H2Frame;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.PixelOperationType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.sablecc2.reactor.imports.ImportUtility;
import prerna.util.ConnectionUtils;
import prerna.util.Utility;

public class FrameCreateAndAddDataReactor extends AbstractReactor {

	/**
	 * This reactor shows how to create a custom frame.
	 * 
	 * To test this reactor you need a database with the movie data.
	 * This reactor can be tested in the pixel console in SEMOSS by running:
	 * FrameCreateAndAddData ( database = [ "<databaseId>" ] ) ;
	 */
	
	
	public FrameCreateAndAddDataReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey() };
	}
	
	@Override
	public NounMetadata execute() {
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		// get the database source
		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		String query = "SELECT TITLE, MOVIEBUDGET, REVENUE_DOMESTIC " 
				+ "FROM TITLE "
				+ "WHERE MOVIEBUDGET > 20000 ";
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
		
		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtils.closeAllConnections(conn, stmt, rs);
		}

		this.insight.setDataMaker(frame);
		NounMetadata retNoun = new NounMetadata(frame, PixelDataType.FRAME, PixelOperationType.FRAME, PixelOperationType.FRAME_HEADERS_CHANGE, PixelOperationType.FRAME_DATA_CHANGE);
		return retNoun;
	}

}
