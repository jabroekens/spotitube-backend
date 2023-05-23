package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import javax.sql.DataSource;

import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withParams;

@ApplicationScoped
public class JdbcTrackRepository implements TrackRepository {

	private static final String FIND_ALL_TRACKS = """
      SELECT
      t.id               AS Track_id,
      t.title            AS Track_title,
      t.duration         AS Track_duration,
      t.offlineAvailable AS Track_offlineAvailable,
      t.playCount        AS Track_playCount,
      t.publicationDate  AS Track_publicationDate,
      t.description      AS Track_description,
      a.name             AS Album_name,
      u.id               AS Performer_id,
      u.passwordHash     AS Performer_passwordHash,
      u.name             AS Performer_name
      FROM Track t
      LEFT JOIN Album a on t.album = a.name
      INNER JOIN Performer p on t.performer = p.id
      INNER JOIN "User" u on p.id = u.id
      """;

	private static final String INSERT_TRACK = """
      INSERT INTO Track (title, performer, duration, offlineavailable, album, playcount, publicationdate, description)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?)
      """;

	private static final String UPDATE_TRACK = """
      UPDATE Track SET
      title=?, performer=?, duration=?, offlineavailable=?,
      album=?,
      playcount=?, publicationdate=?, description=?
      WHERE id=?
      """;

	private DataSource dataSource;

	@Resource(name = "SpotitubeDb")
	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Collection<Track> findAll() throws PersistenceException {
		var tracks = new LinkedList<Track>();

		try (
		  var conn = dataSource.getConnection();
		  var stmt = conn.createStatement();
		  var results = stmt.executeQuery(FIND_ALL_TRACKS)
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
		  var stmt = withParams(conn.prepareStatement(FIND_ALL_TRACKS + " WHERE t.id = ?"), trackId);
		  var results = stmt.executeQuery()
		) {
			return Optional.ofNullable(results.next() ? JdbcHelper.toEntity(Track.class, results) : null);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Track add(Track track) throws PersistenceException {
		if (track.getId().isPresent()) {
			throw new PersistenceException();
		}

		var result = new Track(track);

		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(
			conn.prepareStatement(INSERT_TRACK, Statement.RETURN_GENERATED_KEYS),
			track.getTitle(), track.getPerformer().getId(), track.getDuration(), track.isOfflineAvailable(),
			(track.getAlbum() != null ? track.getAlbum().getName() : null),
			track.getPlayCount(), track.getPublicationDate().orElse(null), track.getDescription().orElse(null)
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

	@Override
	public Track merge(Track track) throws PersistenceException {
		if (track.getId().isEmpty()) {
			throw new PersistenceException();
		}

		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(
			conn.prepareStatement(UPDATE_TRACK),
			track.getTitle(), track.getPerformer().getId(), track.getDuration(), track.isOfflineAvailable(),
			(track.getAlbum() != null ? track.getAlbum().getName() : null),
			track.getPlayCount(), track.getPublicationDate().orElse(null), track.getDescription().orElse(null),
			track.getId()
		  )
		) {
			stmt.executeUpdate();
			return new Track(track);
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


}
