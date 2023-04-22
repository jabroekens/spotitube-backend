package com.github.jabroekens.spotitube.service.api.track.playlist;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.model.track.playlist.PlaylistCollection;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import java.util.List;

public interface PlaylistService {

	/**
	 * {@return the complete and modified collection of playlists}
	 *
	 * @param playlist the playlist to add.
	 *
	 * @throws EntityExistsException if the playerlist to be created already exists.
	 */
	PlaylistCollection createPlaylist(@NotNullAndValid Playlist playlist) throws EntityExistsException;

	/**
	 * {@return the complete collection of playlists}
	 */
	PlaylistCollection getAllPlaylists();

	/**
	 * {@return the complete and modified collection of playlists}
	 *
	 * @param playlist the playlist to modify.
	 *
	 * @throws EntityNotFoundException if the playlist to be modified doesn't exist.
	 */
	PlaylistCollection modifyPlaylist(@NotNullAndValid Playlist playlist) throws EntityNotFoundException;

	/**
	 * {@return the complete and modified collection of playlists}
	 *
	 * @param playlistId the ID of the playlist to remove.
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	PlaylistCollection removePlaylist(@NotNullAndValid String playlistId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist whose tracks to fetch.
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	List<Track> getPlaylistTracks(@NotNullAndValid String playlistId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist to add the track to.
	 * @param trackId    the ID of the track to be added.
	 *
	 * @throws EntityNotFoundException when no playlist or track has been found with the given ID(s).
	 */
	List<Track> addTrackToPlaylist(@NotNullAndValid String playlistId, String trackId) throws EntityNotFoundException;

	/**
	 * {@return the complete list of tracks for the playlist matching the specified ID}
	 *
	 * @param playlistId the ID of the playlist to remove the track from.
	 * @param trackId    the ID of track to be removed.
	 *
	 * @throws EntityNotFoundException when no playlist or track has been found with the given ID(s).
	 */
	List<Track> removeTrackFromPlaylist(@NotNullAndValid String playlistId, @NotNullAndValid String trackId)
	  throws EntityNotFoundException;

}
