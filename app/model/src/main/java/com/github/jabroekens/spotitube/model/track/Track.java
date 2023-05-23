package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * A track is a video or song that can be part of a {@link Playlist}.
 */
@Entity
public class Track {

	// Common
	private Integer id;
	private String title;
	private Performer performer;
	private int duration;
	private boolean offlineAvailable;

	// Song
	private Album album;

	// Video
	private int playCount;
	private LocalDate publicationDate;
	private String description;

	/**
	 * @deprecated Internal no-args constructor used by framework.
	 */
	@Deprecated
	protected Track() {
	}

	/**
	 * Creates a track for a song.
	 *
	 * @param title            the title of the song.
	 * @param performer        the performer of the song.
	 * @param duration         the duration of the song in seconds.
	 * @param offlineAvailable if the track is available for offline use.
	 * @param album            the album the song is a part of.
	 */
	public Track(
		String title,
		Performer performer,
		int duration,
		boolean offlineAvailable,
		Album album
	) {
		this(title, performer, duration, offlineAvailable, album, 0, null, null);
	}

	/**
	 * Creates a track for a video.
	 *
	 * @param title            the title of the video.
	 * @param performer        the publisher of the video.
	 * @param duration         the duration of the video in seconds.
	 * @param offlineAvailable if the track is available for offline use.
	 * @param playCount        the amount of times the video has been played.
	 * @param publicationDate  the publication date of the video. May be {@code null}.
	 * @param description      the description of the video. May be {@code null}.
	 */
	public Track(
	  String title,
	  Performer performer,
	  int duration,
	  boolean offlineAvailable,
	  int playCount,
	  LocalDate publicationDate,
	  String description
	) {
		this(title, performer, duration, offlineAvailable, null, playCount, publicationDate, description);
	}

	/**
	 * Returns a deep copy of {@code track}.
	 */
	public Track(Track track) {
		this(
		  track.title,
		  track.performer != null ? new Performer(track.performer) : null,
		  track.duration,
		  track.offlineAvailable,
		  track.album != null ? new Album(track.album) : null,
		  track.playCount,
		  track.publicationDate,
		  track.description
		);
		this.id = track.id;
	}

	private Track(
	  String title,
	  Performer performer,
	  int duration,
	  boolean offlineAvailable,
	  Album album,
	  int playCount,
	  LocalDate publicationDate,
	  String description
	) {
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.offlineAvailable = offlineAvailable;
		this.album = album;
		this.playCount = playCount;
		this.publicationDate = publicationDate;
		this.description = description;
	}

	public Optional<@GeneratedId Integer> getId() {
		return Optional.ofNullable(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotBlank
	public String getTitle() {
		return title;
	}

	@NotNullAndValid
	public Performer getPerformer() {
		return performer;
	}

	/**
	 * {@return the duration of the track in seconds}
	 */
	@Positive
	public int getDuration() {
		return duration;
	}

	@NotNullAndValid
	public Album getAlbum() {
		return album;
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

	public boolean isOfflineAvailable() {
		return offlineAvailable;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Track track)) return false;
		return id != null
			   && Objects.equals(getId(), track.getId())
			   && getDuration() == track.getDuration()
			   && isOfflineAvailable() == track.isOfflineAvailable()
			   && getPlayCount() == track.getPlayCount()
			   && Objects.equals(getTitle(), track.getTitle())
			   && Objects.equals(getPerformer(), track.getPerformer())
			   && Objects.equals(getAlbum(), track.getAlbum())
			   && Objects.equals(getPublicationDate(), track.getPublicationDate())
			   && Objects.equals(getDescription(), track.getDescription());
	}

	@Override
	public final int hashCode() {
		return Objects.hash(
		  getDuration(),
		  isOfflineAvailable(),
		  getTitle(),
		  getPublicationDate(),
		  getDescription()
		);
	}

}
