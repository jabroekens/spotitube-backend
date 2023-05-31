package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
import java.util.Optional;

/**
 * A track is a video or song that can be part of a {@link Playlist}.
 */
@Entity
public abstract class Track {

	private Integer id;
	private String title;
	private Performer performer;
	private int duration;
	private boolean offlineAvailable;

	/**
	 * @deprecated Internal no-args constructor used by framework.
	 */
	@Deprecated
	protected Track() {
	}

	public abstract Track copy(Track track);

	/**
	 * Returns a deep copy of {@code track}.
	 */
	protected Track(Track track) {
		this(
		  track.title,
		  track.performer != null ? new Performer(track.performer) : null,
		  track.duration,
		  track.offlineAvailable
		);
		this.id = track.id;
	}

	protected Track(
	  String title,
	  Performer performer,
	  int duration,
	  boolean offlineAvailable
	) {
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.offlineAvailable = offlineAvailable;
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

	public boolean isOfflineAvailable() {
		return offlineAvailable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Track track)) return false;
		return id != null
			   && Objects.equals(getId(), track.getId())
			   && getDuration() == track.getDuration()
			   && isOfflineAvailable() == track.isOfflineAvailable()
			   && Objects.equals(getTitle(), track.getTitle())
			   && Objects.equals(getPerformer(), track.getPerformer());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
		  getDuration(),
		  isOfflineAvailable(),
		  getTitle()
		);
	}

}
