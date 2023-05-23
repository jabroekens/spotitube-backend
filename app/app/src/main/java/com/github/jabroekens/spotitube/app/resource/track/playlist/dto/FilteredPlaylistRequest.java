package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest;
import java.util.List;

// Can't be a record; see: https://github.com/FasterXML/jackson-databind/issues/3726#issuecomment-1450950161
public class FilteredPlaylistRequest {

  @JsonUnwrapped
  @JsonIgnoreProperties({"owner", "tracks"})
  public PlaylistRequest playlistRequest;

  public boolean owner;

  public List<FilteredTrack> tracks;

}
