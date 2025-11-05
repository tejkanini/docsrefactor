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
 * This pixel reactor will delete the genre
 */
public class DeleteGenreReactor extends AbstractReactor {
	private static String deleteQuery = "DELETE FROM GENRE WHERE GENRE = ?";

	public DeleteGenreReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "genre" };
	}

	@Override
	public NounMetadata execute() {
		// get pixel inputs
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		String genre = this.keyValue.get(this.keysToGet[1]);
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Must provie a genre");
		}
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

		return NounMetadata.getSuccessNounMessage("Successfully deleted the Genre: " + genre);
	}
}
