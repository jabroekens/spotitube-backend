package com.github.jabroekens.spotitube.persistence.impl_jpa;

import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class TrackRepositoryIT extends IntegrationTestBase {

	private TrackRepository sut;

	// `Tracks.AmericanLove()` and `Tracks.TheEgg()` (and related information) are inserted by `create_tables.sql`
	@BeforeEach
	void setUp() {
		var repository = new JpaTrackRepository();
		repository.setEntityManager(getEntityManager());
		sut = repository;
	}

	@Test
	@Override
	void addsSuccesfully() {
		var track = Tracks.DearNia();
		var savedTrack = sut.add(track);

		assertTrue(savedTrack.getId().isPresent());
		track.setId(savedTrack.getId().get());
		assertMatchesValueInDataStore(track, savedTrack, sut.findById(track.getId().get()));
	}

	@Test
	@Override
	void throwsExceptionWhenAddingExistent() {
		assertThrows(PersistenceException.class, () -> sut.add(Tracks.AmericanLove()));
	}

	@Test
	@Override
	void mergesExistingSuccesfully() {
		var track = Tracks.DearNia();

		// Track is unmodifiable (from a domain perspective),
		// but this mimics modifications
		track.setId(Tracks.AmericanLove().getId().get());

		var mergedTrack = sut.merge(track);
		assertMatchesValueInDataStore(track, mergedTrack, sut.findById(track.getId().get()));
	}

	@Test
	@Override
	void addsWhenMergingNonexistent() {
		var track = Tracks.DearNia();
		var mergedTrack = sut.merge(track);

		assertTrue(mergedTrack.getId().isPresent());
		track.setId(mergedTrack.getId().get());
		assertMatchesValueInDataStore(track, mergedTrack, sut.findById(track.getId().get()));
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

		assertTrue(isEqualCollection(expectedTracks, sut.findAll()));

		expectedTracks.add(sut.add(Tracks.DearNia()));
		assertTrue(isEqualCollection(expectedTracks, sut.findAll()));
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

	@Test
	@Override
	void findsNothingByNonexistentId() {
		assertEquals(Optional.empty(), sut.findById(-1));
	}

}
