package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
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
		var repository = new JdbcPlaylistRepository();
		repository.setDataSource(getDataSource());
		sut = repository;
	}

	@Test
	@Override
	void savesSuccesfully() {
		var savedPlaylist = sut.save(Playlists.Videos());
		assertAll(
		  () -> assertNotEquals("0", savedPlaylist.getId()),
		  // The `equals()` implementation only looks at the business key, so we
		  // must assert all other fields separately: https://stackoverflow.com/a/1638886
		  () -> assertEquals(Playlists.Videos().getName(), savedPlaylist.getName()),
		  () -> assertEquals(Playlists.Videos().getOwner(), savedPlaylist.getOwner()),
		  () -> assertIterableEquals(Playlists.Videos().getTracks(), savedPlaylist.getTracks())
		);
	}

	@Test
	void throwsExceptionWhenInsertingWithNonexistentInformation() {
		var nonexistentTrack = Tracks.DearNia();
		var playlist = Playlists.Videos();
		playlist.addTrack(nonexistentTrack);

		assertThrows(PersistenceException.class, () -> sut.save(playlist));
	}

	@Test
	@Override
	void updatesIfExists() {
		var playlist = Playlists.Empty();
		playlist.addTrack(Tracks.AmericanLove());
		playlist.setName("Songs");

		var savedPlaylist = sut.save(playlist);
		assertAll(
		  () -> assertEquals(playlist.getId(), savedPlaylist.getId()),
		  () -> assertEquals(playlist.getName(), savedPlaylist.getName()),
		  () -> assertIterableEquals(playlist.getTracks(), savedPlaylist.getTracks())
		);
	}

	@Test
	@Override
	void removesIfExists() {
		assertTrue(sut.remove(Playlists.Favorites().getId()));
		assertFalse(sut.remove(Playlists.Favorites().getId()));
	}

	@Test
	@Override
	void findsAll() {
		var playlists = sut.findAll();
		assertEquals(Set.of(Playlists.Empty(), Playlists.Favorites()), playlists);
	}

	@Test
	@Override
	void findsById() {
		var playlist = sut.findById(Playlists.Favorites().getId());
		playlist.ifPresentOrElse(p -> assertEquals(Playlists.Favorites(), p), () -> fail("No value present"));
	}

}
