package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withBatchParams;
import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withParams;

@ApplicationScoped
public class JdbcPlaylistRepository implements PlaylistRepository {

	private static final String FIND_ALL_PLAYLISTS = """
      SELECT
      p.id           AS Playlist_id,
      p.name         AS Playlist_name,
      u.id           AS User_id,
      u.passwordHash AS User_passwordHash,
      u.name         AS User_name
      FROM Playlist p INNER JOIN "User" u ON p.owner = u.id
      """;

	private static final String FIND_ALL_PLAYLIST_TRACKS = """
      SELECT
      pt.playlist        AS PlaylistTrack_playlist,
      t.id               AS Track_id,
      t.title            AS Track_title,
      t.duration         AS Track_duration,
      t.offlineAvailable AS Track_offlineAvailable,
      t.playCount        AS Track_playCount,
      t.publicationDate  AS Track_publicationDate,
      t.description      AS Track_description,
      a.name             AS Album_name,
      u.id               AS User_id,
      u.passwordHash     AS User_passwordHash,
      u.name             AS User_name
      FROM PlaylistTrack pt
      INNER JOIN Track t ON pt.track = t.id
      LEFT JOIN Album a on t.album = a.name
      INNER JOIN Performer p on t.performer = p.id
      INNER JOIN "User" u on p.id = u.id
      """;

	private static final String INSERT_PLAYLIST = "INSERT INTO Playlist (name, owner) VALUES (?, ?)";
	private static final String INSERT_PLAYLIST_TRACKS = "INSERT INTO PlaylistTrack (playlist, track) VALUES (?, ?)";
	private static final String UPDATE_PLAYLIST = "UPDATE Playlist SET name=?, owner=? WHERE id=?";
	private static final String DELETE_PLAYLIST_TRACKS = "DELETE FROM PlaylistTrack WHERE playlist=?";

	private DataSource dataSource;

	@Resource(name = "SpotitubeDb")
	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Collection<Playlist> findAll() throws PersistenceException {
		var playlists = new LinkedHashSet<Playlist>();

		try (
		  var conn = dataSource.getConnection();
		  var stmt = conn.createStatement()
		) {
			try (var results = stmt.executeQuery(FIND_ALL_PLAYLISTS)) {
				while (results.next()) {
					playlists.add(JdbcHelper.toEntity(Playlist.class, results));
				}
			}

			try (var results = stmt.executeQuery(FIND_ALL_PLAYLIST_TRACKS)) {
				while (results.next()) {
					var track = JdbcHelper.toEntity(Track.class, results);
					var playlistId = results.getInt("PlaylistTrack_playlist");

					playlists.stream()
					  .filter(p -> p.getId().equals(Optional.of(playlistId)))
					  .forEach(p -> p.addTrack(track));
				}
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}

		return playlists;
	}

	@Override
	public Optional<Playlist> findById(Integer id) throws PersistenceException {
		Playlist playlist = null;

		try (
		  var conn = dataSource.getConnection();
		  var playlistStmt = withParams(conn.prepareStatement(FIND_ALL_PLAYLISTS + " WHERE p.id = ?"), id);
		  var playlistResults = playlistStmt.executeQuery();
		  var trackStmt = withParams(conn.prepareStatement(FIND_ALL_PLAYLIST_TRACKS + " WHERE pt.playlist = ?"), id);
		  var trackResults = trackStmt.executeQuery()
		) {
			if (playlistResults.next()) {
				playlist = JdbcHelper.toEntity(Playlist.class, playlistResults);

				while (trackResults.next()) {
					playlist.addTrack(JdbcHelper.toEntity(Track.class, trackResults));
				}
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}

		return Optional.ofNullable(playlist);
	}

	@Override
	public Playlist add(Playlist playlist) throws PersistenceException {
		if (playlist.getId().isPresent()) {
			throw new PersistenceException();
		}

		try (var conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			return insertPlaylist(playlist, conn);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	private static Playlist insertPlaylist(Playlist playlist, Connection conn) throws SQLException {
		var result = new Playlist(playlist);

		try (
		  var playlistStmt = withParams(
			conn.prepareStatement(INSERT_PLAYLIST, Statement.RETURN_GENERATED_KEYS),
			playlist.getName(), playlist.getOwner().getId()
		  )
		) {
			playlistStmt.executeUpdate();
			var keys = playlistStmt.getGeneratedKeys();

			if (keys.next()) {
				var playlistId = keys.getInt("id");
				result.setId(playlistId);
				insertTracks(playlist.getTracks(), conn, playlistId);
			}
		} catch (SQLException e) {
			conn.rollback();
		}

		return result;
	}

	private static void insertTracks(List<Track> tracks, Connection conn, int playlistId) throws SQLException {
		// We may assume tracks added to a playlist have
		// been persisted already (and thus have an ID)
		// noinspection OptionalGetWithoutIsPresent
		try (
		  var trackStmt = withBatchParams(
			conn.prepareStatement(INSERT_PLAYLIST_TRACKS),
			tracks.stream()
			  .map(t -> new Object[]{playlistId, t.getId().get()})
			  .toArray(Object[][]::new)
		  )
		) {
			trackStmt.executeUpdate();
			conn.commit();
		}
	}

	@Override
	public Playlist merge(Playlist playlist) throws PersistenceException {
		if (playlist.getId().isEmpty()) {
			throw new PersistenceException();
		}

		try (var conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			return mergePlaylist(playlist, conn, playlist.getId().get());
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	private static Playlist mergePlaylist(Playlist playlist, Connection conn, int playlistId) throws SQLException {
		try (
		  var playlistStmt = withParams(
			conn.prepareStatement(UPDATE_PLAYLIST),
			playlist.getName(), playlist.getOwner().getId(), playlistId
		  )
		) {
			playlistStmt.executeUpdate();
			reinsertTracks(playlist.getTracks(), conn, playlistId);
		} catch (SQLException e) {
			conn.rollback();
		}

		return playlist;
	}

	private static void reinsertTracks(List<Track> tracks, Connection conn, int playlistId) throws SQLException {
		// We may assume tracks added to a playlist have
		// been persisted already (and thus have an ID)
		// noinspection OptionalGetWithoutIsPresent
		try (
		  var deleteTracksStmt = withParams(
			conn.prepareStatement(DELETE_PLAYLIST_TRACKS),
			playlistId
		  );
		  var insertTracksStmt = withBatchParams(
			conn.prepareStatement(INSERT_PLAYLIST_TRACKS),
			tracks.stream()
			  .map(t -> new Object[]{playlistId, t.getId().get()})
			  .toArray(Object[][]::new)
		  )
		) {
			deleteTracksStmt.executeUpdate();
			insertTracksStmt.executeUpdate();
		}
	}

	@Override
	public boolean remove(Integer id) throws PersistenceException {
		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(conn.prepareStatement("DELETE FROM Playlist WHERE id=?"), id)
		) {
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}


}
