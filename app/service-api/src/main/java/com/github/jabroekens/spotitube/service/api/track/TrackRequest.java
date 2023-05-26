package com.github.jabroekens.spotitube.service.api.track;

import com.github.jabroekens.spotitube.model.user.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.format.DateTimeFormatter;

public record TrackRequest(
  int id,
  @NotBlank String title,
  @UserId String performerId,
  @Positive int duration,
  boolean offlineAvailable,
  @NotBlank String albumId,
  @PositiveOrZero int playCount,
  String publicationDate,
  String description
) {

	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

}
