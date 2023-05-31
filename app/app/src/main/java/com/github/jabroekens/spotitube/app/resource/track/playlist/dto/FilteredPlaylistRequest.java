package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.github.jabroekens.spotitube.app.resource.track.dto.FilteredTrackRequest;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.List;

public record FilteredPlaylistRequest(
  int id,
  String name,
  boolean owner,
  List<FilteredTrackRequest> tracks
) {

  public FilteredPlaylistRequest(Playlist playlist, String authenticatedUser) {
    this(
      playlist.getId().orElseThrow(),
      playlist.getName(),
      playlist.getOwner().getId().equals(authenticatedUser),
      playlist.getTracks().stream().map(FilteredTrackRequest::fromTrack).toList()
    );
  }

}
