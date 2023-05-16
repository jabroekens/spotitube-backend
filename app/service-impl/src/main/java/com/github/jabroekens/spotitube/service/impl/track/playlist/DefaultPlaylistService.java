package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistCollection;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistService;
import jakarta.inject.Inject;
import java.util.List;

public class DefaultPlaylistService implements PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final TrackRepository trackRepository;

	@Inject
	public DefaultPlaylistService(PlaylistRepository playlistRepository, TrackRepository trackRepository) {
		this.playlistRepository = playlistRepository;
		this.trackRepository = trackRepository;
	}

	@Override
	public PlaylistCollection createPlaylist(Playlist playlist) throws EntityExistsException {
		// FIXME race condition
		if (playlistRepository.findById(playlist.getId()).isPresent()) {
			throw new EntityExistsException(Playlist.class, playlist.getId());
		}

		playlistRepository.save(playlist);
		return getAllPlaylists();
	}

	@Override
	public PlaylistCollection getAllPlaylists() {
		var playlists = playlistRepository.findAll();
		return new PlaylistCollection(playlists.size(), playlists);
	}

	@Override
	public PlaylistCollection modifyPlaylist(Playlist playlist) throws EntityNotFoundException {
		// FIXME race condition
		if (playlistRepository.findById(playlist.getId()).isEmpty()) {
			throw new EntityNotFoundException(Playlist.class, playlist.getId());
		}

		playlistRepository.save(playlist);
		return getAllPlaylists();
	}

	@Override
	public PlaylistCollection removePlaylist(String playlistId) throws EntityNotFoundException {
		if (!playlistRepository.remove(playlistId)) {
			throw new EntityNotFoundException(Playlist.class, playlistId);
		}

		return getAllPlaylists();
	}

	@Override
	public List<Track> getPlaylistTracks(String playlistId) throws EntityNotFoundException {
		return playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, playlistId))
		  .getTracks();
	}

	@Override
	public List<Track> addTrackToPlaylist(String playlistId, String trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, playlistId));

		var track = trackRepository.findById(trackId)
		  .orElseThrow(() -> new EntityNotFoundException(Track.class, trackId));

		playlist.addTrack(track);
		return playlistRepository.save(playlist).getTracks();
	}

	@Override
	public List<Track> removeTrackFromPlaylist(String playlistId, String trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, playlistId));

		if (!playlist.removeTrack(trackId)) {
			throw new EntityNotFoundException(Track.class, trackId);
		}

		return playlistRepository.save(playlist).getTracks();
	}

}
