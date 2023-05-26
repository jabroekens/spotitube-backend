package com.github.jabroekens.spotitube.app.resource.track.dto;

import com.github.jabroekens.spotitube.model.track.Track;
import java.util.Collection;
import java.util.List;

public record FilteredTracksResponse(
  List<FilteredTrackRequest> tracks
) {

	public FilteredTracksResponse(Collection<Track> tracks) {
		this(tracks.stream().map(FilteredTrackRequest::new).toList());
	}

}
