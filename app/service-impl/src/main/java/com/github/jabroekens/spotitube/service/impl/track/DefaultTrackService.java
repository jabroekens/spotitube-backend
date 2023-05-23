package com.github.jabroekens.spotitube.service.impl.track;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.track.TrackService;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistService;
import jakarta.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultTrackService implements TrackService {

	private final TrackRepository trackRepository;
	private final PlaylistService playlistService;

	@Inject
	public DefaultTrackService(TrackRepository trackRepository, PlaylistService playlistService) {
		this.trackRepository = trackRepository;
		this.playlistService = playlistService;
	}

	@Override
	public List<Track> getAvailableTracks() throws EntityNotFoundException {
		return trackRepository.findAll().stream().toList();
	}

	@Override
	public List<Track> getAvailableTracks(int playlistId) throws EntityNotFoundException {
		var playlistTracks = playlistService.getPlaylistTracks(playlistId);
		return getAvailableTracks().stream()
		  .filter(t -> !playlistTracks.contains(t))
		  .collect(Collectors.toCollection(LinkedList::new));
	}

}
