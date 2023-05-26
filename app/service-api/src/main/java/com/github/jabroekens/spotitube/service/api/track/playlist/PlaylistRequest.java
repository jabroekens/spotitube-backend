package com.github.jabroekens.spotitube.service.api.track.playlist;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.user.UserId;
import com.github.jabroekens.spotitube.service.api.track.TrackRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PlaylistRequest(
  int id,
  @NotBlank String name,
  @UserId String ownerId,
  @NotNull List<@NotNullAndValid TrackRequest> tracks
) {

}
