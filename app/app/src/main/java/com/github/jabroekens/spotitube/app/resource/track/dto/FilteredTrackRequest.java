package com.github.jabroekens.spotitube.app.resource.track.dto;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.service.api.track.TrackRequest;

public record FilteredTrackRequest(
  int id,
  String title,
  String performer,
  int duration,
  boolean offlineAvailable,
  String album,
  Integer playCount,
  String publicationDate,
  String description
) {

  public FilteredTrackRequest(Track track) {
    this(
      track.getId().orElseThrow(),
      track.getTitle(),
      track.getPerformer().getName(),
      track.getDuration(),
      track.isOfflineAvailable(),
      (track.getAlbum() != null ? track.getAlbum().getName() : null),
      track.getPlayCount(),
      track.getPublicationDate().map(pd -> pd.format(TrackRequest.DATE_FORMAT)).orElse(null),
      track.getDescription().orElse(null)
    );
  }

}
