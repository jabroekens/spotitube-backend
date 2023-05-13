package com.github.jabroekens.spotitube.model.track.playlist;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * A collection of {@link Playlist playlists}.
 *
 * @param length    the total length in seconds of all the tracks in the playlists.
 * @param playlists the playlists.
 */
public record PlaylistCollection(
	@PositiveOrZero int length,
	Collection<@NotNullAndValid Playlist> playlists
) {

}
