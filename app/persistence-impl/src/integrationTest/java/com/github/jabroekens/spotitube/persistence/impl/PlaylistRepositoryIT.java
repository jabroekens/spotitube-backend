package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
		var savedPlaylist = sut.add(Playlists.Videos());
		assertPlaylist(
		  Playlists.Videos(), savedPlaylist,
		  () -> assertTrue(savedPlaylist.getId().isPresent())
		);
	}

	@Test
	@Override
	void mergesSuccesfully() {
		var playlist = Playlists.Empty();
		playlist.addTrack(Tracks.AmericanLove());
		playlist.setName("Songs");

		var savedPlaylist = sut.merge(playlist);
		assertPlaylist(playlist, savedPlaylist);
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
		var playlists = sut.findAll();
		assertEquals(Set.of(Playlists.Empty(), Playlists.Favorites()), playlists);
	}

	@Test
	@Override
	void findsById() {
		var playlist = sut.findById(Playlists.Favorites().getId().get());
		playlist.ifPresentOrElse(p -> assertEquals(Playlists.Favorites(), p), () -> fail("No value present"));
	}

	private static void assertPlaylist(Playlist expected, Playlist actual, Executable... additionalAssertions) {
		// The `equals()` implementation only looks at the business key, so we
		// must assert all other fields separately: https://stackoverflow.com/a/1638886
		assertAll(
		  Stream.concat(
			Stream.of(additionalAssertions),
			Stream.of(
			  () -> assertEquals(expected.getName(), actual.getName()),
			  () -> assertEquals(expected.getOwner(), actual.getOwner()),
			  () -> assertIterableEquals(expected.getTracks(), actual.getTracks())
			)
		  )
		);
	}

}
