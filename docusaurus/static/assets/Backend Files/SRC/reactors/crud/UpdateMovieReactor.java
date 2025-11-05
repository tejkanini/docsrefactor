package reactors.crud;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.ConnectionUtils;
import prerna.util.Utility;

public class UpdateMovieReactor extends AbstractReactor {
	private static final String titleUpdate = "UPDATE TITLE SET TITLE = ? WHERE TITLE = ?";
	private static final String nominatedUpdate = "UPDATE NOMINATED SET TITLE_FK = ? WHERE TITLE_FK = ?";
	private static final String genreUpdate = "UPDATE GENRE SET TITLE_FK = ? WHERE TITLE_FK = ?";

	public UpdateMovieReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "title", "newTitle" };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		// get inputs and add data validation
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		String title = this.keyValue.get(this.keysToGet[1]);
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Must provide a title");
		}
		String newTitle = this.keyValue.get(this.keysToGet[2]);
		if (newTitle == null || newTitle.isEmpty()) {
			throw new IllegalArgumentException("Must provide a new title");
		}

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

		return NounMetadata.getSuccessNounMessage("Updated title: " + newTitle);
	}
}
