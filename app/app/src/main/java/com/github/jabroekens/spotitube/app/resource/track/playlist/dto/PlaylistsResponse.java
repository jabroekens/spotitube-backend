package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * A collection of {@link FilteredPlaylist playlists}.
 *
 * @param length    the total length in seconds of all the tracks in the playlists.
 * @param playlists the playlists.
 */
public record PlaylistsResponse(
  int length,
  Collection<FilteredPlaylist> playlists
) {

	public static PlaylistsResponse fromPlaylists(Collection<Playlist> playlists, String authenticatedUser) {
		return new PlaylistsResponse(
		  playlists.stream().flatMapToInt(PlaylistsResponse::getTracksDurationStream).sum(),
		  playlists.stream().map(p -> FilteredPlaylist.fromPlaylist(p, authenticatedUser)).toList()
		);
	}

	private static IntStream getTracksDurationStream(Playlist playlist) {
		return playlist.getTracks().stream().mapToInt(Track::getDuration);
	}

}
