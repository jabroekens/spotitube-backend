package com.github.jabroekens.spotitube.app.resource.track.dto;

import com.github.jabroekens.spotitube.model.track.Song;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.Video;
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

  public static FilteredTrackRequest fromTrack(Track track) {
    if (track instanceof Song s) return new FilteredTrackRequest(s);
    if (track instanceof Video v) return new FilteredTrackRequest(v);
    throw new IllegalArgumentException("Unmappable track type");
  }

  public FilteredTrackRequest(Song song) {
    this(
      song.getId().orElseThrow(),
      song.getTitle(),
      song.getPerformer().getName(),
      song.getDuration(),
      song.isOfflineAvailable(),
      (song.getAlbum() != null ? song.getAlbum().getName() : null),
      null,
      null,
      null
    );
  }

  public FilteredTrackRequest(Video video) {
    this(
      video.getId().orElseThrow(),
      video.getTitle(),
      video.getPerformer().getName(),
      video.getDuration(),
      video.isOfflineAvailable(),
      null,
      video.getPlayCount(),
      video.getPublicationDate().map(pd -> pd.format(TrackRequest.DATE_FORMAT)).orElse(null),
      video.getDescription().orElse(null)
    );
  }

}
