package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PlaylistRepositoryIT extends IntegrationTestBase {

	private PlaylistRepository sut;

	// Playlists.EMPTY and Playlists.FAVORITE (and related information) are inserted by `create_tables.sql`
	@BeforeEach
	void setUp() {
		var repository = new DefaultPlaylistRepository();
		repository.setDataSource(getDataSource());
		sut = repository;
	}

	@Test
	void insertsNewPlaylistSuccesfullyAndSetsId() {
		var savedPlaylist = sut.save(Playlists.VIDEOS);
		assertAll(
		  () -> assertNotEquals("0", savedPlaylist.getId()),
		  // The `equals()` implementation only looks at the business key, so we
		  // must assert all other fields separately: https://stackoverflow.com/a/1638886
		  () -> assertEquals(Playlists.VIDEOS.getName(), savedPlaylist.getName()),
		  () -> assertEquals(Playlists.VIDEOS.getOwner(), savedPlaylist.getOwner()),
		  () -> assertIterableEquals(Playlists.VIDEOS.getTracks(), savedPlaylist.getTracks())
		);
	}

	@Test
	void throwsExceptionWhenInsertingPlaylistWithNonexistentInformation() {
		var nonexistentTrack = Tracks.DEAR_NIA;
		var playlist = new Playlist(Playlists.VIDEOS);
		playlist.addTrack(nonexistentTrack);

		assertThrows(PersistenceException.class, () -> sut.save(playlist));
	}

	@Test
	void updatesPlaylistIfExists() {
		var playlist = new Playlist(Playlists.EMPTY);
		playlist.addTrack(Tracks.AMERICAN_LOVE);
		playlist.setName("Songs");

		var savedPlaylist = sut.save(playlist);
		assertAll(
		  () -> assertEquals(playlist.getId(), savedPlaylist.getId()),
		  () -> assertEquals(playlist.getName(), savedPlaylist.getName()),
		  () -> assertIterableEquals(playlist.getTracks(), savedPlaylist.getTracks())
		);
	}

	@Test
	void removesPlaylistIfExists() {
		assertTrue(sut.remove(Playlists.FAVORITES.getId()));
		assertFalse(sut.remove(Playlists.FAVORITES.getId()));
	}

	@Test
	void findsAllPlaylists() {
		var playlists = sut.findAll();
		assertEquals(Set.of(Playlists.EMPTY, Playlists.FAVORITES), playlists);
	}

	@Test
	void findsPlaylistById() {
		var playlist = sut.findById(Playlists.FAVORITES.getId());
		playlist.ifPresentOrElse(p -> assertEquals(Playlists.FAVORITES, p), () -> fail("No value present"));
	}

}
