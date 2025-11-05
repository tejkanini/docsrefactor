package reactors.crud;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prerna.engine.impl.rdbms.RDBMSNativeEngine;
import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.ConnectionUtils;
import prerna.util.Constants;
import prerna.util.Utility;

/**
 * This pixel reactor will make two insertions into the database
 * 1) add a new Movie
 * 2) add the nomination of the Movie, default No
 */
public class AddMovieReactor extends AbstractReactor {
	private static final Logger logger = LogManager.getLogger(AddMovieReactor.class);

	// queries to add values to movie and nomination table
	private static String insertMovieQuery = "INSERT INTO TITLE (TITLE, MOVIEBUDGET, REVENUE_DOMESTIC, REVENUE_INTERNATIONAL, ROTTENTOMATOES_AUDIENCE, ROTTENTOMATOES_CRITICS) VALUES (?, ?, ?, ?, ?, ?)";
	private static String insertNominatedQuery = "INSERT INTO NOMINATED (TITLE_FK, NOMINATED) VALUES (?, ?)";

	public AddMovieReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey(), "map" };
	}

	@Override
	public NounMetadata execute() {
		// grab inputs from pixel
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);
		Map<String, Object> titleInfo = getMap();
		// add data validation for pixel inputs
		String title = (String) titleInfo.get("title");
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Must provide a title");
		}
		Number movieBudget = (Number) titleInfo.get("movieBudget");
		if (movieBudget == null || movieBudget.doubleValue() < 0) {
			throw new IllegalArgumentException("Must provide a Movie Budget >= 0");
		}
		Number rottenTomatoesAudience = (Number) titleInfo.get("rtAudience");
		Number rottenTomatoesCritics = (Number) titleInfo.get("rtCritics");
		Number revenueDomestic = (Number) titleInfo.get("revenueDomestic");
		Number revenueInternational = (Number) titleInfo.get("revenueInternational");

		RDBMSNativeEngine database = (RDBMSNativeEngine) Utility.getEngine(databaseId);
		database.setAutoCommit(false);
		PreparedStatement ps = null;
		PreparedStatement nomPS = null;

		try {
			int i = 1;
			// add values Movie table
			ps = database.getPreparedStatement(AddMovieReactor.insertMovieQuery);
			ps.setString(i++, title);
			ps.setDouble(i++, movieBudget.doubleValue());
			ps.setDouble(i++, revenueDomestic.doubleValue());
			ps.setDouble(i++, revenueInternational.doubleValue());
			ps.setDouble(i++, rottenTomatoesAudience.doubleValue());
			ps.setDouble(i++, rottenTomatoesCritics.doubleValue());
			// add movie to Nomination table with default value
			nomPS = database.getPreparedStatement(AddMovieReactor.insertNominatedQuery);
			nomPS.setString(1, title);
			nomPS.setString(2, "No");
			ps.execute();
			nomPS.execute();
			database.commit();
		} catch (SQLException e) {
			logger.error(Constants.STACKTRACE, e);
			return NounMetadata.getErrorNounMessage("SQL ERROR: " + e.getMessage());
		}
		finally {
			ConnectionUtils.closePreparedStatement(ps);
			ConnectionUtils.closePreparedStatement(nomPS);
		}

		return NounMetadata.getSuccessNounMessage("Added new Movie");
	}
	
	private Map<String, Object> getMap() {
		GenRowStruct mapGrs = this.store.getNoun("map");
		if (mapGrs != null && !mapGrs.isEmpty()) {
			List<NounMetadata> mapInputs = mapGrs.getNounsOfType(PixelDataType.MAP);
			if (mapInputs != null && !mapInputs.isEmpty()) {
				return (Map<String, Object>) mapInputs.get(0).getValue();
			}
		}

		List<NounMetadata> mapInputs = this.curRow.getNounsOfType(PixelDataType.MAP);
		if (mapInputs != null && !mapInputs.isEmpty()) {
			return (Map<String, Object>) mapInputs.get(0).getValue();
		}

		throw new IllegalArgumentException("Invalid title info");
	}
}