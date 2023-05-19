package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
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

		assertTrack(
		  track, savedTrack,
		  () -> assertTrue(savedTrack.getId().isPresent())
		);
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
		var tracks = sut.findAll();
		assertIterableEquals(Set.of(Tracks.AmericanLove(), Tracks.TheEgg()), tracks);
	}

	@Test
	@Override
	void findsById() {
		var track = sut.findById(Tracks.AmericanLove().getId().get());
		track.ifPresentOrElse(p -> assertEquals(Tracks.AmericanLove(), p), () -> fail("No value present"));
	}

	private static void assertTrack(Track expected, Track actual, Executable... additionalAssertions) {
		// The `equals()` implementation only looks at the business key, so we
		// must assert all other fields separately: https://stackoverflow.com/a/1638886
		assertAll(
		  Stream.concat(
			Stream.of(additionalAssertions),
			Stream.of(
			  () -> assertEquals(expected.getTitle(), actual.getTitle()),
			  () -> assertEquals(expected.getPerformer(), actual.getPerformer()),
			  () -> assertEquals(expected.getDuration(), actual.getDuration()),
			  () -> assertEquals(expected.isOfflineAvailable(), actual.isOfflineAvailable()),
			  () -> assertEquals(expected.getAlbum(), actual.getAlbum()),
			  () -> assertEquals(expected.getPlayCount(), actual.getPlayCount()),
			  () -> assertEquals(expected.getPublicationDate(), actual.getPublicationDate()),
			  () -> assertEquals(expected.getDescription(), actual.getDescription())
			)
		  )
		);
	}

}
