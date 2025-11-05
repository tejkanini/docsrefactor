package reactors.crud;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.ConnectionUtils;
import prerna.util.Utility;

/**
 * This pixel reactor will perform two deletions in the database 
 * 1) delete the movie 
 * 2) delete the nomination info of the movie
 */
public class DeleteMovieReactor extends AbstractReactor {
	private static String deleteMovie = "DELETE FROM TITLE WHERE TITLE = ?";
	private static String deleteMovieNomination = "DELETE FROM NOMINATED WHERE TITLE_FK = ?";

	public DeleteMovieReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "title" };
	}

	@Override
	public NounMetadata execute() {
		// get pixel inputs
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		String title = this.keyValue.get(this.keysToGet[1]);
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Must provie a title");
		}
		// delete genre from the database
		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		database.setAutoCommit(false);
		PreparedStatement ps = null;
		PreparedStatement psNom = null;

		try {
			// delete title from title table
			ps = database.getPreparedStatement(DeleteMovieReactor.deleteMovie);
			ps.setString(1, title);
			// delete title from nomination table
			psNom = database.getPreparedStatement(DeleteMovieReactor.deleteMovieNomination);
			psNom.setString(1, title);
			ps.execute();
			psNom.execute();
			database.commit();
		} catch (SQLException e) {
			return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
		} finally {
			ConnectionUtils.closePreparedStatement(ps);
			ConnectionUtils.closePreparedStatement(psNom);
		}

		return NounMetadata.getSuccessNounMessage("Successfully deleted the Title: " + title);
	}
}
