package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * A collection of {@link FilteredPlaylistRequest playlists}.
 *
 * @param length    the total length in seconds of all the tracks in the playlists.
 * @param playlists the playlists.
 */
public record FilteredPlaylistsResponse(
  int length,
  Collection<FilteredPlaylistRequest> playlists
) {

	public FilteredPlaylistsResponse(Collection<Playlist> playlists, String authenticatedUser) {
		this(
		  playlists.stream().flatMapToInt(FilteredPlaylistsResponse::getTracksDurationStream).sum(),
		  playlists.stream().map(p -> new FilteredPlaylistRequest(p, authenticatedUser)).toList()
		);
	}

	private static IntStream getTracksDurationStream(Playlist playlist) {
		return playlist.getTracks().stream().mapToInt(Track::getDuration);
	}

}
