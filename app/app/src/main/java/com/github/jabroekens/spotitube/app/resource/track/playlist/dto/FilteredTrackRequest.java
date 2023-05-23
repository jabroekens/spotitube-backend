package com.github.jabroekens.spotitube.app.resource.track.playlist.dto;

public record FilteredTrackRequest(
  int id,
  String title,
  String performer,
  int duration
) {

}
