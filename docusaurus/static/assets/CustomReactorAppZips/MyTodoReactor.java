package mytodo.reactor;

import prerna.reactor.AbstractReactor;
import prerna.engine.api.IRDBMSEngine;
import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.PixelOperationType;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyTodoReactor extends AbstractReactor {
	
	private static final Logger logger = LogManager.getLogger(MyTodoReactor.class);
	
	public MyTodoReactor() {
		this.keysToGet = new String[] {"database", "values"};
		this.keyRequired = new int[] {1, 1};
	}
	
	@Override
	public NounMetadata execute() {
		// parse values
		organizeKeys();
		String db = this.keyValue.get("database");
		GenRowStruct valGrs = this.store.getNoun("values");
		String taskName = (String) valGrs.get(0);
		String taskId = (String) valGrs.get(1);
		String taskStatus = (String) valGrs.get(2);
		
		// delete the original task by ID		
		IRDBMSEngine engine = (IRDBMSEngine) Utility.getEngine(db);
		Connection con = null;
		try {
			con = engine.makeConnection();
			boolean isAutoCommit = con.getAutoCommit();
			
			try {
				String sql = "DELETE FROM MY_TODO WHERE TASK_ID = " + taskId;
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.execute();
				}

				if (!"Complete".equalsIgnoreCase(taskStatus)) {
					String sql2 = "INSERT INTO MY_TODO (TASK_ID, TASK, STATUS) VALUES (?, ?, ?)";

					try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
						ps2.setString(1, taskId);
						ps2.setString(2, taskName);
						ps2.setString(3, taskStatus);
						ps2.execute();
					}
				}
				con.commit();
			} catch (SQLException | IllegalArgumentException e) {
				con.rollback();
				logger.error("Error occurred updating the row. Detailed message: " + e.getMessage());
			} finally {
				con.setAutoCommit(isAutoCommit);
			}
		} catch (SQLException e) {
			logger.error("Error occurred updating the row. Detailed message: " + e.getMessage());
		}
	
		return new NounMetadata(true, PixelDataType.BOOLEAN, PixelOperationType.ALTER_DATABASE, PixelOperationType.FORCE_SAVE_DATA_TRANSFORMATION);
	}

}
