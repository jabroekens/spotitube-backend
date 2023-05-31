package com.github.jabroekens.spotitube.model.track.playlist;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.GeneratedId;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A playlist can consist of one or more {@link Track track(s)}.
 */
@Entity
public class Playlist {

	private Integer id;
	private String name;
	private User owner;

	@SuppressWarnings("FieldMayBeFinal")
	private List<Track> tracks;

	/**
	 * @deprecated Internal no-args constructor used by framework.
	 */
	@Deprecated
	protected Playlist() {
		this.tracks = new LinkedList<>();
	}

	public Playlist(String name, User owner, List<Track> tracks) {
		this.name = name;
		this.owner = owner;
		this.tracks = new LinkedList<>(tracks);
	}

	/**
	 * Returns a deep copy of {@code playlist}.
	 */
	public Playlist(Playlist playlist) {
		this(
		  playlist.name,
		  playlist.owner != null ? new User(playlist.owner) : null,
		  playlist.tracks.stream().map(t -> t.copy(t)).collect(Collectors.toCollection(LinkedList::new))
		);
		this.id = playlist.id;
	}

	public Optional<@GeneratedId Integer> getId() {
		return Optional.ofNullable(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNullAndValid
	public User getOwner() {
		return owner;
	}

	@NotNull
	public List<@NotNullAndValid Track> getTracks() {
		return tracks;
	}

	/**
	 * Adds the specified track to this playlist.
	 *
	 * @param track the track to be added.
	 * @return {@code true} if the track was added, {@code false} otherwise.
	 */
	public boolean addTrack(Track track) {
		return tracks.add(track);
	}

	/**
	 * Removes the track with ID {@code trackId} from this playlist.
	 *
	 * @param trackId the ID of the track to be removed.
	 *
	 * @return {@code true} if the track was removed, {@code false} false otherwise.
	 */
	public boolean removeTrack(int trackId) {
		return tracks.removeIf(t -> t.getId().equals(Optional.of(trackId)));
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Playlist playlist)) return false;
		return id != null
			   && Objects.equals(getId(), playlist.getId())
			   && Objects.equals(getName(), playlist.getName())
			   && Objects.equals(getOwner(), playlist.getOwner())
			   && Objects.equals(getTracks(), playlist.getTracks());
	}

	@Override
	public final int hashCode() {
		return 13;
	}

}
