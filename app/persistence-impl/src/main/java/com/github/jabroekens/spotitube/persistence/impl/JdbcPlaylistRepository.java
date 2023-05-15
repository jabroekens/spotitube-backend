package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import javax.sql.DataSource;

import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withBatchParams;
import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withParams;

@ApplicationScoped
public class JdbcPlaylistRepository implements PlaylistRepository {

	private static final String FIND_ALL_PLAYLISTS = """
	  SELECT p.id AS Playlist_id, p.name AS Playlist_name,
	  u.id AS User_id, u.passwordHash AS User_passwordHash, u.name AS User_name
	  FROM Playlist p INNER JOIN "User" u ON p.owner = u.id
	  """;

	private static final String FIND_ALL_PLAYLIST_TRACKS = """
	  SELECT pt.playlist AS PlaylistTrack_playlist,
	  t.id AS Track_id, t.title AS Track_title, t.duration AS Track_duration, t.offlineAvailable AS Track_offlineAvailable,
	  t.playCount AS Track_playCount, t.publicationDate AS Track_publicationDate, t.description AS Track_description,
	  a.name AS Album_name, u.id AS User_id, u.passwordHash AS User_passwordHash, u.name AS User_name
	  FROM PlaylistTrack pt
	  INNER JOIN Track t ON pt.track = t.id
	  LEFT JOIN Album a on t.album = a.name
	  INNER JOIN Performer p on t.performer = p.id
	  INNER JOIN "User" u on p.id = u.id
	  """;

	private static final String SAVE_PLAYLIST = """
	  INSERT INTO Playlist (name, owner)
	  VALUES (?, ?)
	  ON CONFLICT (id) DO UPDATE SET name=?, owner=?
	  RETURNING id
	  """;

	private static final String SAVE_PLAYLIST_TRACK = """
	  INSERT INTO PlaylistTrack (playlist, track)
	  VALUES (?, ?)
	  """;

	private DataSource dataSource;

	@Resource(name = "SpotitubeDb")
	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Collection<Playlist> findAll() {
		try (
		  var conn = dataSource.getConnection();
		  var stmt = conn.createStatement()
		) {
			var playlists = new LinkedHashSet<Playlist>();

			try (var results = stmt.executeQuery(FIND_ALL_PLAYLISTS)) {
				while (results.next()) {
					playlists.add(JdbcHelper.toEntity(Playlist.class, results));
				}
			}

			try (var results = stmt.executeQuery(FIND_ALL_PLAYLIST_TRACKS)) {
				while (results.next()) {
					var value = JdbcHelper.toEntity(Track.class, results);
					var playlistId = results.getString("PlaylistTrack_playlist");
					playlists.stream().filter(p -> p.getId().equals(playlistId)).forEach(p -> p.addTrack(value));
				}
			}

			return playlists;
		} catch (SQLException | RuntimeException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<Playlist> findById(String playlistId) {
		try (var conn = dataSource.getConnection()) {
			var intPlaylistId = Integer.parseInt(playlistId);
			Playlist playlist = null;

			try (
			  var stmt = withParams(conn.prepareStatement(FIND_ALL_PLAYLISTS + " WHERE p.id = ?"), intPlaylistId);
			  var results = stmt.executeQuery()
			) {
				if (results.next()) {
					playlist = JdbcHelper.toEntity(Playlist.class, results);

					try (
					  var trackStmt = withParams(
						conn.prepareStatement(FIND_ALL_PLAYLIST_TRACKS + " WHERE pt.playlist = ?"),
						intPlaylistId
					  );
					  var trackResults = trackStmt.executeQuery()
					) {
						while (trackResults.next()) {
							var value = JdbcHelper.toEntity(Track.class, trackResults);
							playlist.addTrack(value);
						}
					}
				}
			}

			return Optional.ofNullable(playlist);
		} catch (SQLException | RuntimeException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Playlist save(Playlist playlist) {
		try (var conn = dataSource.getConnection()) {
			try (
			  var playlistStmt = withParams(
				conn.prepareStatement(SAVE_PLAYLIST, Statement.RETURN_GENERATED_KEYS),
				playlist.getName(), playlist.getOwner().getId(),
				playlist.getName(), playlist.getOwner().getId()
			  )
			) {
				playlistStmt.executeUpdate();
				var keys = playlistStmt.getGeneratedKeys();

				if (keys.next()) {
					var playlistId = keys.getInt("id");

					try (
					  var trackStmt = withBatchParams(
						conn.prepareStatement(SAVE_PLAYLIST_TRACK),
						playlist.getTracks().stream()
						  .map(t -> new Object[]{playlistId, Integer.parseInt(t.getId())})
						  .toArray(Object[][]::new)
					  )
					) {
						trackStmt.executeUpdate();
					}

					playlist.setId(playlistId);
				}
			}

			return playlist;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public boolean remove(String id) {
		try (
		  var conn = dataSource.getConnection();
		  var stmt = withParams(conn.prepareStatement("DELETE FROM Playlist WHERE id=?"), Integer.parseInt(id))
		) {
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}


}
