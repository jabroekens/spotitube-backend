package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.Track;
import java.time.LocalDate;

public final class Tracks {

	public static Track AmericanLove() {
		return withId(1, new Track("American Love", Performers.Smallpools(), 179, true, Albums.Lovetap()));
	}

	public static Track TheEgg() {
		return withId(2, new Track(
		  "The Egg - A Short Story", Performers.Kurzgesagt(), 474,
		  false, 28613533,
		  LocalDate.of(2019, 9, 1), "The Egg. Story by Andy Weir, Animated by Kurzgesagt"
		));
	}

	public static Track DearNia() {
		return new Track(
		  "Dear Nia", Performers.Exurb1a(), 677,
		  false, 1397268,
		  LocalDate.of(2022, 12, 3),
		  "This is not a good summation of the many-worlds interpretation of quantum mechanics." +
			" This is what happens when you stay up reading about it while imbibing moderate-to-inadvisable amounts of beer."
		);
	}

	private static Track withId(int id, Track track) {
		track.setId(id);
		return track;
	}

}
