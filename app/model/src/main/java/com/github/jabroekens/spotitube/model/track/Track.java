package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * A track is a video or song that can be part of a {@link Playlist}.
 */
public class Track {

	private final String id;
	private final String title;
	private final Performer performer;
	private final int duration;
	private final Album album;
	private final int playCount;
	private final ZonedDateTime publicationDate;
	private final String description;
	private final boolean offlineAvailable;

	/**
	 * Creates a track for a song.
	 *
	 * @param id               the ID of the track.
	 * @param title            the title of the song.
	 * @param performer        the performer of the song.
	 * @param duration         the duration of the song in seconds.
	 * @param album            the album the song is a part of.
	 * @param offlineAvailable if the track is available for offline use.
	 */
	public Track(
		String id,
		String title,
		Performer performer,
		int duration,
		Album album,
		boolean offlineAvailable
	) {
		this(id, title, performer, duration, 0, album, null, null, offlineAvailable);
	}

	/**
	 * Creates a track for a video.
	 *
	 * @param id               the ID of the track.
	 * @param title            the title of the video.
	 * @param performer        the publisher of the video.
	 * @param duration         the duration of the video in seconds.
	 * @param playCount        the amount of times the video has been played.
	 * @param publicationDate  the publication date of the video.
	 * @param description      the description of the video.
	 * @param offlineAvailable if the track is available for offline use.
	 */
	public Track(
		String id,
		String title,
		Performer performer,
		int duration,
		int playCount,
		ZonedDateTime publicationDate,
		String description,
		boolean offlineAvailable
	) {
		this(id, title, performer, duration, playCount, null, publicationDate, description, offlineAvailable);
	}

	private Track(
		String id,
		String title,
		Performer performer,
		int duration,
		int playCount,
		Album album,
		ZonedDateTime publicationDate,
		String description,
		boolean offlineAvailable
	) {
		this.id = id;
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.album = album;
		this.playCount = playCount;
		this.publicationDate = publicationDate;
		this.description = description;
		this.offlineAvailable = offlineAvailable;
	}

	@Id
	public String getId() {
		return id;
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

}
