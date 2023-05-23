package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Tracks;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackTest {

	@Test
	void equalsFollowsContract() {
		EqualsVerifier.forClass(Track.class)
		  .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
		  .suppress(Warning.STRICT_HASHCODE)
		  .suppress(Warning.NONFINAL_FIELDS)
		  .verify();
	}

	@Test
	void givesDeepCloneWithCopyConstructor() {
		var t1 = Tracks.DearNia();
		var t2 = new Track(t1);

		// Tracks without an ID will always be unequal
		t1.setId(0);
		t2.setId(0);

		assertEquals(t1, t2);
	}

}
