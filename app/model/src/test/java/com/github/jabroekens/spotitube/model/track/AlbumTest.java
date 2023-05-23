package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Albums;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlbumTest {

	@Test
	void equalsFollowsContract() {
		EqualsVerifier.forClass(Album.class)
		  .suppress(Warning.NONFINAL_FIELDS)
		  .withOnlyTheseFields("name")
		  .verify();
	}

	@Test
	void givesDeepCloneWithCopyConstructor() {
		var a1 = Albums.Lovetap();
		var a2 = new Album(a1);
		assertEquals(a1, a2);
	}

}
