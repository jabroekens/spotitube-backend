package com.github.jabroekens.spotitube.persistence.api;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.model.user.User;
import java.util.Collection;

public interface PlaylistRepository extends Repository<Playlist, Integer> {

	/**
	 * Finds the {@link Playlist playlists} belonging to the {@link User} whose ID matches {@code userId}.
	 *
	 * @return the complete collection of playlists belonger to the specified user.
	 */
	Collection<Playlist> findByUser(String userId) throws PersistenceException;

}
