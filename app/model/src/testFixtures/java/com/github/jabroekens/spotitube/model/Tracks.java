package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.Track;
import java.time.LocalDate;
import java.time.ZoneOffset;

public final class Tracks {

	public static final Track AMERICAN_LOVE = new Track(1, "American Love", Performers.SMALLPOOLS, 179, Albums.LOVETAP, true);

	public static final Track THE_EGG = new Track(
	  2, "The Egg - A Short Story", Performers.KURZGESAGT, 474,
	  28613533, LocalDate.of(2019, 9, 1).atStartOfDay(ZoneOffset.UTC),
	  "The Egg. Story by Andy Weir, Animated by Kurzgesagt", false
	);

	public static final Track DEAR_NIA = new Track(
	  3, "Dear Nia", Performers.EXURB1A, 677,
	  1397268, LocalDate.of(2022, 12, 3).atStartOfDay(ZoneOffset.UTC),
	  "This is not a good summation of the many-worlds interpretation of quantum mechanics." +
		" This is what happens when you stay up reading about it while imbibing moderate-to-inadvisable amounts of beer.",
	  false
	);

}
