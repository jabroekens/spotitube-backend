package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import java.util.List;

public record FilteredPlaylist(
  @JsonUnwrapped
  @JsonIgnoreProperties({"owner", "tracks"})
  Playlist playlist,
  List<FilteredTrack> tracks,
  boolean owner
) {

  public static FilteredPlaylist fromPlaylist(Playlist playlist, String authenticatedUser) {
    return new FilteredPlaylist(
      playlist,
      playlist.getTracks().stream().map(FilteredTrack::fromTrack).toList(),
      playlist.getOwner().getId().equals(authenticatedUser)
    );
  }

}
