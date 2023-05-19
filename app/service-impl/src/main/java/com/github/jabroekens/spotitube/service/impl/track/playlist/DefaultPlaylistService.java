package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistCollection;
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
		try {
			playlistRepository.add(playlist);
		} catch (PersistenceException e) {
			// We may assume an ID is set (see `Repository#add(T)` Javadoc)
			// noinspection OptionalGetWithoutIsPresent
			throw new EntityExistsException(Playlist.class, playlist.getId().get().toString());
		}
		return getAllPlaylists();
	}

	@Override
	public PlaylistCollection getAllPlaylists() {
		var playlists = playlistRepository.findAll();
		return new PlaylistCollection(playlists.size(), playlists);
	}

	@Override
	public PlaylistCollection modifyPlaylist(Playlist playlist) throws EntityNotFoundException {
		try {
			playlistRepository.merge(playlist);
		} catch (PersistenceException e) {
			throw new EntityNotFoundException(Playlist.class, "name=%s, owner=%s".formatted(playlist.getName(), playlist.getOwner().getId()));
		}
		return getAllPlaylists();
	}

	@Override
	public PlaylistCollection removePlaylist(int playlistId) throws EntityNotFoundException {
		if (!playlistRepository.remove(playlistId)) {
			throw new EntityNotFoundException(Playlist.class, "id=%s".formatted(playlistId));
		}

		return getAllPlaylists();
	}

	@Override
	public List<Track> getPlaylistTracks(int playlistId) throws EntityNotFoundException {
		return playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, "id=%s".formatted(playlistId)))
		  .getTracks();
	}

	@Override
	public List<Track> addTrackToPlaylist(int playlistId, int trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, "id=%s".formatted(playlistId)));

		var track = trackRepository.findById(trackId)
		  .orElseThrow(() -> new EntityNotFoundException(Track.class, "id=%s".formatted(trackId)));

		playlist.addTrack(track);
		return playlistRepository.add(playlist).getTracks();
	}

	@Override
	public List<Track> removeTrackFromPlaylist(int playlistId, int trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, "id=%s".formatted(playlistId)));

		if (!playlist.removeTrack(trackId)) {
			throw new EntityNotFoundException(Track.class, "id=%s".formatted(trackId));
		}

		return playlistRepository.add(playlist).getTracks();
	}

}
