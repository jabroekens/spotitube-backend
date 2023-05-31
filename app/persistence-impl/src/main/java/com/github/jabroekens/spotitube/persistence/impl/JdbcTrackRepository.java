package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import jakarta.annotation.Resource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;

import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withParams;

public abstract class JdbcTrackRepository implements TrackRepository {

	private static final String FIND_ALL_TRACKS = """
	  SELECT
	  t.id               AS Track_id,
	  t.title            AS Track_title,
	  t.duration         AS Track_duration,
	  t.offlineAvailable AS Track_offlineAvailable,
	  u.id               AS Performer_id,
	  u.passwordHash     AS Performer_passwordHash,
	  u.name             AS Performer_name
	  %s
	  FROM Track t
	  INNER JOIN Performer p on t.performer = p.id
	  INNER JOIN "User" u on p.id = u.id
	  %s
	  """;

	protected static final String INSERT_TRACK = """
	  INSERT INTO Track (title, performer, duration, offlineavailable)
	  VALUES (?, ?, ?, ?)
	  """;

	protected static final String UPDATE_TRACK = """
	  UPDATE Track SET
	  title=?, performer=?, duration=?, offlineavailable=?
	  WHERE id=?
	  """;

	protected static String buildFindAllQuery(String sqlColumnAddition, String sqlJoinAddition) {
		return FIND_ALL_TRACKS.formatted(sqlColumnAddition, sqlJoinAddition);
	}

	private DataSource dataSource;

	@Resource(name = "SpotitubeDb")
	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public abstract String findAllQueryString();

	@Override
	public Collection<Track> findAll() throws PersistenceException {
		var tracks = new LinkedList<Track>();

		try (
		  var conn = dataSource.getConnection();
		  var stmt = conn.createStatement();
		  var results = stmt.executeQuery(findAllQueryString())
		) {
			while (results.next()) {
				tracks.add(JdbcHelper.toEntity(Track.class, results));
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}

		return tracks;
	}

	@Override
	public Optional<Track> findById(Integer trackId) throws PersistenceException {
		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(conn.prepareStatement(findAllQueryString() + " WHERE t.id = ?"), trackId);
		  var results = stmt.executeQuery()
		) {
			return Optional.ofNullable(results.next() ? JdbcHelper.toEntity(Track.class, results) : null);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	public abstract String insertQueryString();

	public abstract Object[] insertParams();

	@Override
	public Track add(Track track) throws PersistenceException {
		if (track.getId().isPresent()) {
			throw new PersistenceException();
		}

		var result = track.copy(track);

		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(
			conn.prepareStatement(insertQueryString(), Statement.RETURN_GENERATED_KEYS),
			prependVarargs(insertParams(), track.getTitle(), track.getPerformer().getId(), track.getDuration(), track.isOfflineAvailable())
		  )
		) {
			stmt.executeUpdate();
			var keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				result.setId(keys.getInt("id"));
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}

		return result;
	}

	public abstract String updateQueryString();

	public abstract Object[] updateParams();

	@Override
	public Track merge(Track track) throws PersistenceException {
		var trackId = track.getId();
		if (trackId.isEmpty()) {
			return add(track);
		}

		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(
			conn.prepareStatement(updateQueryString()),
			prependVarargs(
			  updateParams(), track.getTitle(), track.getPerformer().getId(),
			  track.getDuration(), track.isOfflineAvailable(), trackId.get()
			)
		  )
		) {
			stmt.executeUpdate();
			return track.copy(track);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public boolean remove(Integer id) throws PersistenceException {
		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(conn.prepareStatement("DELETE FROM Track WHERE id=?"), id)
		) {
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	/**
	 * Returns a new array whose values are the result of prepending {@code values} to {@code array}'s values.
	 */
	private static Object[] prependVarargs(Object[] array, Object... values) {
		return Stream.concat(
		  Arrays.stream(values),
		  Arrays.stream(array)
		).toArray();
	}

}
