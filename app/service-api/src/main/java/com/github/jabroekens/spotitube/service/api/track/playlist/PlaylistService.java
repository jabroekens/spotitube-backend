package com.github.jabroekens.spotitube.service.api.track.playlist;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.GeneratedId;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

public interface PlaylistService {

	/**
	 * {@return the created playlist}
	 *
	 * @param playlistRequest the playlist to add.
	 *
	 * @throws EntityNotFoundException if the owner of the playlist to be created does not exist.
	 * @throws EntityExistsException if the playlist to be created already exists.
	 */
	Playlist createPlaylist(@NotNullAndValid PlaylistRequest playlistRequest)
	  throws EntityNotFoundException, EntityExistsException;

	/**
	 * {@return the complete collection of playlists}
	 */
	Collection<Playlist> getAllPlaylists();

	/**
	 * {@return the modified playlist}
	 *
	 * @param playlistRequest the playlist to modify.
	 *
	 * @throws EntityNotFoundException if the owner of the playlist to be created does not exist.
	 * @throws EntityNotFoundException if the playlist to be modified doesn't exist.
	 */
	Playlist modifyPlaylist(@NotNullAndValid PlaylistRequest playlistRequest)
	  throws EntityNotFoundException, EntityExistsException;

	/**
	 * Removes the playlist whose ID is {@code playlistId}.
	 *
	 * @param playlistId the ID of the playlist to remove.
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	void removePlaylist(@GeneratedId int playlistId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist whose tracks to fetch.
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	List<Track> getPlaylistTracks(@GeneratedId int playlistId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist to add the track to.
	 * @param trackId    the ID of the track to be added.
	 *
	 * @throws EntityNotFoundException when no playlist or track has been found with the given ID(s).
	 */
	List<Track> addTrackToPlaylist(@GeneratedId int playlistId, @GeneratedId int trackId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist to remove the track from.
	 * @param trackId    the ID of track to be removed.
	 *
	 * @throws EntityNotFoundException when no playlist or track has been found with the given ID(s).
	 */
	List<Track> removeTrackFromPlaylist(@GeneratedId int playlistId, @GeneratedId int trackId)
	  throws EntityNotFoundException;

}
