package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.jabroekens.spotitube.model.track.Track;
import java.time.format.DateTimeFormatter;

// Can't be a record; see: https://github.com/FasterXML/jackson-databind/issues/3726#issuecomment-1450950161
public class FilteredTrack {

  @JsonUnwrapped
  @JsonIgnoreProperties({"performer", "album", "publicationDate"})
  public Track track;

  public String performer;

  public String album;

  public String publicationDate;

  public static FilteredTrack fromTrack(Track track) {
    var filteredTrack = new FilteredTrack();
    filteredTrack.track = track;
    filteredTrack.performer = track.getPerformer().getName();

    if (track.getAlbum() != null) {
      filteredTrack.album = track.getAlbum().getName();
    }

    if (track.getPublicationDate().isPresent()) {
      filteredTrack.publicationDate = track.getPublicationDate().get().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    return filteredTrack;
  }

}
