package com.github.jabroekens.spotitube.model.track.playlist;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class PlaylistTest {

	@Test
	void equalsFollowsContract() {
		EqualsVerifier.forClass(Playlist.class)
		  .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
		  .suppress(Warning.STRICT_HASHCODE)
		  .suppress(Warning.NONFINAL_FIELDS)
		  .verify();
	}

	@Test
	void addsTrack() {
		var playlist = Playlists.Empty();
		var track = Tracks.DearNia();
		assertTrue(playlist.addTrack(track));
		assertIterableEquals(List.of(track), playlist.getTracks());
	}

	@Test
	void removesTrack() {
		var playlist = Playlists.Favorites();
		var track = playlist.getTracks().get(0);
		assertTrue(playlist.removeTrack(track.getId().get()));
		assertFalse(playlist.getTracks().contains(track));
	}

	@Test
	void givesDeepCloneWithCopyConstructor() {
		var p1 = Playlists.Favorites();
		var p2 = new Playlist(p1);
		assertEquals(p1, p2);
	}

}
