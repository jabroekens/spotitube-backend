package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class PlaylistRepositoryIT extends IntegrationTestBase {

	private PlaylistRepository sut;

	// `Playlists.Empty()` and `Playlists.Favorite()` (and related information) are inserted by `create_tables.sql`
	@BeforeEach
	void setUp() {
		var repository = new JdbcPlaylistRepository();
		repository.setDataSource(getDataSource());
		sut = repository;
	}

	@Test
	@Override
	void addsSuccesfully() {
		var playlist = Playlists.Videos();
		var savedPlaylist = sut.add(playlist);

		assertTrue(savedPlaylist.getId().isPresent());
		playlist.setId(savedPlaylist.getId().get());
		assertMatchesValueInDataStore(playlist, savedPlaylist, sut.findById(playlist.getId().get()));
	}

	@Test
	@Override
	void throwsExceptionWhenAddingExistent() {
		assertThrows(PersistenceException.class, () -> sut.add(Playlists.Empty()));
	}

	@Test
	@Override
	void mergesExistingSuccesfully() {
		var playlist = Playlists.Empty();
		playlist.addTrack(Tracks.AmericanLove());
		playlist.setName("Songs");

		var mergedPlaylist = sut.merge(playlist);
		assertMatchesValueInDataStore(playlist, mergedPlaylist, sut.findById(playlist.getId().get()));
	}

	@Test
	@Override
	void addsWhenMergingNonexistent() {
		var playlist = Playlists.Videos();
		var mergedPlaylist = sut.merge(playlist);

		assertTrue(mergedPlaylist.getId().isPresent());
		playlist.setId(mergedPlaylist.getId().get());
		assertMatchesValueInDataStore(playlist, mergedPlaylist, sut.findById(playlist.getId().get()));
	}

	@Test
	@Override
	void removesIfExists() {
		assertTrue(sut.remove(Playlists.Favorites().getId().get()));
		assertFalse(sut.remove(Playlists.Favorites().getId().get()));
	}

	@Test
	@Override
	void findsAll() {
		var expectedPlaylists = Stream.of(
		  Playlists.Empty(), Playlists.Favorites()
		).collect(Collectors.toCollection(ArrayList::new));

		assertIterableEquals(expectedPlaylists, sut.findAll());

		expectedPlaylists.add(sut.add(Playlists.Videos()));
		assertIterableEquals(expectedPlaylists, sut.findAll());
	}

	@Test
	@Override
	void findsById() {
		var existingPlaylist = Playlists.Favorites();
		var addedPlaylist = sut.add(Playlists.Videos());

		var playlist1 = sut.findById(existingPlaylist.getId().get());
		var playlist2 = sut.findById(addedPlaylist.getId().get());

		assertAll(
		  () -> playlist1.ifPresentOrElse(u -> assertEquals(existingPlaylist, u), () -> fail("No value present")),
		  () -> playlist2.ifPresentOrElse(u -> assertEquals(addedPlaylist, u), () -> fail("No value present"))
		);
	}

	@Test
	@Override
	void findsNothingByNonexistentId() {
		assertEquals(Optional.empty(), sut.findById(-1));
	}

}
