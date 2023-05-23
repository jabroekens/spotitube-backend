package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Performers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PerformerTest {

	@Test
	void givesDeepCloneWithCopyConstructor() {
		var p1 = Performers.Kurzgesagt();
		var p2 = new Performer(p1);
		assertEquals(p1, p2);
	}

}
