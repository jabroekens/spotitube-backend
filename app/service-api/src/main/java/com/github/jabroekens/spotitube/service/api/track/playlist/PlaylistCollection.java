package com.github.jabroekens.spotitube.service.api.track.playlist;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collection;

/**
 * A collection of {@link Playlist playlists}.
 *
 * @param length    the total length in seconds of all the tracks in the playlists.
 * @param playlists the playlists.
 */
public record PlaylistCollection(
	int length,
	Collection<Playlist> playlists
) {

}
