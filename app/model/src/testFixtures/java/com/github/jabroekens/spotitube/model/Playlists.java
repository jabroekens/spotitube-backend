package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collections;
import java.util.List;

public final class Playlists {

	public static Playlist Empty() {
		return withId(1, new Playlist("Empty", Users.JohnDoe(), Collections.emptyList()));
	}

	public static Playlist Favorites() {
		return withId(2, new Playlist("Favorites", Users.JohnDoe(), List.of(Tracks.AmericanLove(), Tracks.TheEgg())));
	}

	public static Playlist Videos() {
		return new Playlist("Videos", Users.JohnDoe(), List.of(Tracks.AmericanLove()));
	}

	private static Playlist withId(int id, Playlist playlist) {
		playlist.setId(id);
		return playlist;
	}

}
