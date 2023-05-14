package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collections;
import java.util.List;

public final class Playlists {

	public static final Playlist EMPTY = new Playlist(1, "Empty", Users.JOHN_DOE, Collections.emptyList());

	public static final Playlist FAVORITES = new Playlist(2, "Favorites", Users.JOHN_DOE, List.of(
	  Tracks.AMERICAN_LOVE,
	  Tracks.THE_EGG
	));

	// 'id' will be generated and changed from 0
	public static final Playlist VIDEOS = new Playlist(0, "Favorites", Users.JOHN_DOE, List.of(
	  Tracks.AMERICAN_LOVE
	));

}
