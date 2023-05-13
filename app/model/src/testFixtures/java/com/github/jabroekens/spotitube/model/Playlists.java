package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collections;

public final class Playlists {

	public static Playlist EMPTY = new Playlist("1", "Empty", Users.DEFAULT, Collections.emptyList());

}
