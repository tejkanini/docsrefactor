package reactors.crud;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prerna.engine.api.IRawSelectWrapper;
import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.query.querystruct.SelectQueryStruct;
import prerna.query.querystruct.filters.SimpleQueryFilter;
import prerna.query.querystruct.selectors.QueryColumnSelector;
import prerna.rdf.engine.wrappers.WrapperManager;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.ConnectionUtils;
import prerna.util.Constants;
import prerna.util.Utility;

/**
 * This pixel reactor will add a Genre for a movie.
 * - if a genre exists for a title, it will throw an error
 */
public class AddGenreReactor extends AbstractReactor {
	private static final Logger logger = LogManager.getLogger(AddGenreReactor.class);
	private static String insertQuery = "INSERT INTO GENRE (GENRE, TITLE_FK) VALUES (?, ?)";

	public AddGenreReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "genre", "title" };
	}

	@Override
	public NounMetadata execute() {
		organizeKeys();
		// get inputs and add data validation
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		String genre = this.keyValue.get(this.keysToGet[1]);
		if (genre == null || genre.isEmpty()) {
			throw new IllegalArgumentException("Must provie a genre");
		}
		String title = this.keyValue.get(this.keysToGet[2]);
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Must provide a title");
		}

		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		// check if title has a genre
		checkExistingGenre(database, title);
		// add new genre
		PreparedStatement ps = null;
		try {
			int i = 1;
			ps = database.getPreparedStatement(AddGenreReactor.insertQuery);
			ps.setString(i++, genre);
			ps.setString(i++, title);
			ps.execute();
			database.commit();
		} catch (SQLException e) {
			logger.error(Constants.STACKTRACE, e);
			return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
		} finally {
			ConnectionUtils.closePreparedStatement(ps);
		}

		return NounMetadata.getSuccessNounMessage("Added new Genre: " + genre);
	}

	private void checkExistingGenre(RDBMSNativeEngine database, String title) {
		SelectQueryStruct qs = new SelectQueryStruct();
		qs.addSelector(new QueryColumnSelector("GENRE__GENRE"));
		qs.addExplicitFilter(SimpleQueryFilter.makeColToValFilter("GENRE__TITLE_FK", "==", title));
		IRawSelectWrapper iterator = null;
		try {
			iterator = WrapperManager.getInstance().getRawWrapper(database, qs);
			if (iterator.hasNext()) {
				throw new IllegalArgumentException("This movie has an existing genre, please update instead.");
			}
		} catch (Exception e) {
			logger.error(Constants.STACKTRACE, e);
		} finally {
			if (iterator != null) {
				iterator.cleanUp();
			}
		}
	}
}
