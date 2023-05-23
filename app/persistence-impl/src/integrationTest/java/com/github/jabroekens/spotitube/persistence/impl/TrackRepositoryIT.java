package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class TrackRepositoryIT extends IntegrationTestBase {

	private TrackRepository sut;

	// `Tracks.AmericanLove()` and `Tracks.TheEgg()` (and related information) are inserted by `create_tables.sql`
	@BeforeEach
	void setUp() {
		var repository = new JdbcTrackRepository();
		repository.setDataSource(getDataSource());
		sut = repository;
	}

	@Test
	@Override
	void addsSuccesfully() {
		var track = Tracks.DearNia();
		var savedTrack = sut.add(track);

		assertTrue(savedTrack.getId().isPresent());
		track.setId(savedTrack.getId().get());
		assertEquals(track, savedTrack);
	}

	@Test
	@Override
	void mergesSuccesfully() {
		// Track is unmodifiable (from a domain perspective)
	}

	@Test
	@Override
	void removesIfExists() {
		assertTrue(sut.remove(Tracks.AmericanLove().getId().get()));
		assertFalse(sut.remove(Tracks.AmericanLove().getId().get()));
	}

	@Test
	@Override
	void findsAll() {
		var expectedTracks = Stream.of(
		 Tracks.AmericanLove(), Tracks.TheEgg()
		).collect(Collectors.toCollection(ArrayList::new));

		assertIterableEquals(expectedTracks, sut.findAll());

		expectedTracks.add(sut.add(Tracks.DearNia()));
		assertIterableEquals(expectedTracks, sut.findAll());
	}

	@Test
	@Override
	void findsById() {
		var existingTrack = Tracks.AmericanLove();
		var addedTrack = sut.add(Tracks.DearNia());

		var track1 = sut.findById(existingTrack.getId().get());
		var track2 = sut.findById(addedTrack.getId().get());

		assertAll(
		  () -> track1.ifPresentOrElse(u -> assertEquals(existingTrack, u), () -> fail("No value present")),
		  () -> track2.ifPresentOrElse(u -> assertEquals(addedTrack, u), () -> fail("No value present"))
		);
	}

}
