package com.github.jabroekens.spotitube.model.track.playlist;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.Id;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * A playlist can consist of one or more {@link Track track(s)}.
 */
public class Playlist {

	private final String id;
	private String name;
	private final User owner;
	private final List<Track> tracks;

	public Playlist(String id, String name, User owner, List<Track> tracks) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.tracks = tracks;
	}

	@Id
	public String getId() {
		return id;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(@NotBlank String name) {
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
	public boolean addTrack(@NotNullAndValid Track track) {
		return getTracks().add(track);
	}

	/**
	 * Removes the track with ID {@code trackId} from this playlist.
	 *
	 * @param trackId the ID of the track to be removed.
	 * @return {@code true} if the track was removed, {@code false} false otherwise.
	 */
	public boolean removeTrack(String trackId) {
		return getTracks().removeIf(t -> t.getId().equals(trackId));
	}

}
