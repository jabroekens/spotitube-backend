package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * A track is a video or song that can be part of a {@link Playlist}.
 */
@Entity
public class Track {

	private Integer id;
	private String title;
	private Performer performer;
	private int duration;
	private boolean offlineAvailable;

	private Album album;

	private int playCount;
	private ZonedDateTime publicationDate;
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
	 * @param album            the album the song is a part of.
	 * @param offlineAvailable if the track is available for offline use.
	 */
	public Track(
		String title,
		Performer performer,
		int duration,
		Album album,
		boolean offlineAvailable
	) {
		this(title, performer, duration, 0, album, null, null, offlineAvailable);
	}

	/**
	 * Creates a track for a video.
	 *
	 * @param title            the title of the video.
	 * @param performer        the publisher of the video.
	 * @param duration         the duration of the video in seconds.
	 * @param playCount        the amount of times the video has been played.
	 * @param publicationDate  the publication date of the video. May be {@code null}.
	 * @param description      the description of the video. May be {@code null}.
	 * @param offlineAvailable if the track is available for offline use.
	 */
	public Track(
	  String title,
	  Performer performer,
	  int duration,
	  int playCount,
	  ZonedDateTime publicationDate,
	  String description,
	  boolean offlineAvailable
	) {
		this(title, performer, duration, playCount, null, publicationDate, description, offlineAvailable);
	}

	/**
	 * Returns a deep copy of {@code track}.
	 */
	public Track(Track track) {
		this.id = track.id;
		this.title = track.title;
		this.performer = new Performer(track.performer);
		this.duration = track.duration;
		this.offlineAvailable = track.offlineAvailable;

		this.album = track.album != null ? new Album(track.album) : null;

		this.playCount = track.playCount;
		this.publicationDate = track.publicationDate;
		this.description = track.description;
	}

	private Track(
	  String title,
	  Performer performer,
	  int duration,
	  int playCount,
	  Album album,
	  ZonedDateTime publicationDate,
	  String description,
	  boolean offlineAvailable
	) {
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.album = album;
		this.playCount = playCount;
		this.publicationDate = publicationDate;
		this.description = description;
		this.offlineAvailable = offlineAvailable;
	}

	public Optional<@GeneratedId Integer> getId() {
		return Optional.ofNullable(id);
	}

	public void setId(@GeneratedId int id) {
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

	@Valid
	public Album getAlbum() {
		return album;
	}

	@PositiveOrZero
	public int getPlayCount() {
		return playCount;
	}

	public Optional<ZonedDateTime> getPublicationDate() {
		return Optional.ofNullable(publicationDate);
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(description);
	}

	public boolean isOfflineAvailable() {
		return offlineAvailable;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Track track)) return false;
		return Objects.equals(getId(), track.getId());
	}

	@Override
	public final int hashCode() {
		return Objects.hash(getId());
	}

}
