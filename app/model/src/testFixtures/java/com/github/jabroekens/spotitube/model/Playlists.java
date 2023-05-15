package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collections;
import java.util.List;

public final class Playlists {

	public static Playlist Empty() {
		return new Playlist(1, "Empty", Users.JohnDoe(), Collections.emptyList());
	}

	public static Playlist Favorites() {
		return new Playlist(2, "Favorites", Users.JohnDoe(), List.of(Tracks.AmericanLove(), Tracks.TheEgg()));
	}

	public static Playlist Videos() {
		// 'id' will be generated and changed from 0
		return new Playlist(0, "Favorites", Users.JohnDoe(), List.of(Tracks.AmericanLove()));
	}

}
