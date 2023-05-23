package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest;
import java.util.List;

// Can't be a record; see: https://github.com/FasterXML/jackson-databind/issues/3726#issuecomment-1450950161
public class FilteredPlaylistRequest {

  @JsonUnwrapped
  @JsonIgnoreProperties({"owner", "tracks"})
  private PlaylistRequest playlistRequest;

  private boolean owner;

  private List<FilteredTrack> tracks;

  public PlaylistRequest playlistRequest() {
    return playlistRequest;
  }

  public boolean owner() {
    return owner;
  }

  public List<FilteredTrack> tracks() {
    return tracks;
  }

}
