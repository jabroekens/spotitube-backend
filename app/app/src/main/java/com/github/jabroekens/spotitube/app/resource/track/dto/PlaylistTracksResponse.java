package com.github.jabroekens.spotitube.app.resource.track.dto;

import com.github.jabroekens.spotitube.app.resource.track.playlist.dto.FilteredTrack;
import com.github.jabroekens.spotitube.model.track.Track;
import java.util.List;

public record PlaylistTracksResponse(
  List<FilteredTrack> tracks
) {

	public static PlaylistTracksResponse fromTracks(List<Track> tracks) {
		var filteredTracks = tracks.stream().map(FilteredTrack::fromTrack).toList();
		return new PlaylistTracksResponse(filteredTracks);
	}

}
