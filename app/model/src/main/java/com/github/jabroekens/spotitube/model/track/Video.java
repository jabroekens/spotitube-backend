package com.github.jabroekens.spotitube.model.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("FieldMayBeFinal")
public class Video extends Track {

	private int playCount;
	private LocalDate publicationDate;
	private String description;

	/**
	 * Creates a video.
	 *
	 * @param title            the title of the video.
	 * @param performer        the publisher of the video.
	 * @param duration         the duration of the video in seconds.
	 * @param offlineAvailable if the track is available for offline use.
	 * @param playCount        the amount of times the video has been played.
	 * @param publicationDate  the publication date of the video. May be {@code null}.
	 * @param description      the description of the video. May be {@code null}.
	 */
	public Video(
	  String title,
	  Performer performer,
	  int duration,
	  boolean offlineAvailable,
	  int playCount,
	  LocalDate publicationDate,
	  String description
	) {
		super(title, performer, duration, offlineAvailable);
		this.playCount = playCount;
		this.publicationDate = publicationDate;
		this.description = description;
	}

	/**
	 * Returns a deep copy of {@code video}.
	 */
	public Video(Video video) {
		super(video);
		this.playCount = video.playCount;
		this.publicationDate = video.publicationDate;
		this.description = video.description;
	}

	@PositiveOrZero
	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public Optional<LocalDate> getPublicationDate() {
		return Optional.ofNullable(publicationDate);
	}

	public Optional<@NotBlank String> getDescription() {
		return Optional.ofNullable(description);
	}

	@Override
	public Track copy(Track track) {
		return track instanceof Video v
		  ? new Video(v)
		  : track.copy(track);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Video video)) return false;
		if (!super.equals(o)) return false;
		return getPlayCount() == video.getPlayCount()
			   && Objects.equals(getPublicationDate(), video.getPublicationDate())
			   && Objects.equals(getDescription(), video.getDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getPublicationDate(), getDescription());
	}

}
