package reactors.crud;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.ConnectionUtils;
import prerna.util.Utility;

public class UpdateGenreReactor extends AbstractReactor {
	private static final String genreUpdate = "UPDATE GENRE SET GENRE = ? WHERE GENRE = ?";

	public UpdateGenreReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "genre", "newGenre" };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		// get inputs and add data validation
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		String genre = this.keyValue.get(this.keysToGet[1]);
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Must provide a genre");
		}
		String newGenre = this.keyValue.get(this.keysToGet[2]);
		if (newGenre == null || newGenre.isEmpty()) {
			throw new IllegalArgumentException("Must provide a new genre");
		}

		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		PreparedStatement ps = null;
		try {
			int i = 1;
			ps = database.getPreparedStatement(UpdateGenreReactor.genreUpdate);
			ps.setString(1, newGenre);
			ps.setString(2, genre);
			ps.execute();
			database.commit();
		} catch (SQLException e) {
			return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
		} finally {
			ConnectionUtils.closePreparedStatement(ps);
		}

		return NounMetadata.getSuccessNounMessage("Updated genre: " + genre);
	}
}
