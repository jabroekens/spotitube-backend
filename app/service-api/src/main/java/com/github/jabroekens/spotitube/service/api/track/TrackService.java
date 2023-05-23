package com.github.jabroekens.spotitube.service.api.track;

import com.github.jabroekens.spotitube.model.track.GeneratedId;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import java.util.List;

public interface TrackService {

	/**
	 * {@return all available tracks}
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	List<Track> getAvailableTracks() throws EntityNotFoundException;

	/**
	 * {@return the available tracks not already in the playlist matching the specified ID}
	 *
	 * @param playlistId the playlist whose tracks to exclude from the result.
	 *
	 * @throws EntityNotFoundException when no playlist has been found with ID {@code playlistId}.
	 */
	List<Track> getAvailableTracks(@GeneratedId int playlistId) throws EntityNotFoundException;

}
