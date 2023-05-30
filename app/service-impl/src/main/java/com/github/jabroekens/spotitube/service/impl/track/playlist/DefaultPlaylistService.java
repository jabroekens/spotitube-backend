package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.model.user.UserId;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.track.TrackRequest;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistService;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultPlaylistService implements PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final UserRepository userRepository;
	private final TrackRepository trackRepository;

	@Inject
	public DefaultPlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, TrackRepository trackRepository) {
		this.playlistRepository = playlistRepository;
		this.userRepository = userRepository;
		this.trackRepository = trackRepository;
	}

	@Override
	public Playlist createPlaylist(PlaylistRequest playlistRequest) throws EntityExistsException {
		var owner = userRepository.findById(playlistRequest.ownerId());
		if (owner.isPresent()) {
			var playlist = new Playlist(playlistRequest.name(), owner.get(), toTracks(playlistRequest.tracks()));
			if (playlistRequest.id() > 0) {
				playlist.setId(playlistRequest.id());
			}

			try {
				return playlistRepository.add(playlist);
			} catch (PersistenceException e) {
				throw new EntityExistsException(Playlist.class, playlist.getId().orElseThrow());
			}
		} else {
			throw new EntityNotFoundException(User.class, Map.of("id", playlistRequest.ownerId()));
		}
	}

	@Override
	public Collection<Playlist> getAllPlaylists() {
		return playlistRepository.findAll();
	}

	@Override
	public Collection<Playlist> getUserPlaylists(@UserId String userId) {
		return playlistRepository.findByUser(userId);
	}

	@Override
	public Playlist modifyPlaylist(PlaylistRequest playlistRequest) throws EntityNotFoundException {
		var owner = userRepository.findById(playlistRequest.ownerId());
		if (owner.isPresent()) {
			var playlist = new Playlist(playlistRequest.name(), owner.get(), toTracks(playlistRequest.tracks()));
			playlist.setId(playlistRequest.id());

			try {
				return playlistRepository.merge(playlist);
			} catch (PersistenceException e) {
				throw new EntityNotFoundException(Playlist.class, Map.of("name", playlist.getName(), "owner", playlist.getOwner().getId()));
			}
		} else {
			throw new EntityNotFoundException(User.class, Map.of("id", playlistRequest.ownerId()));
		}
	}

	@Override
	public void removePlaylist(int playlistId) throws EntityNotFoundException {
		if (!playlistRepository.remove(playlistId)) {
			throw new EntityNotFoundException(Playlist.class, Map.of("id", playlistId));
		}
	}

	@Override
	public List<Track> getPlaylistTracks(int playlistId) throws EntityNotFoundException {
		return playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, Map.of("id", playlistId)))
		  .getTracks();
	}

	@Override
	public List<Track> addTrackToPlaylist(int playlistId, int trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, Map.of("id", playlistId)));

		var track = trackRepository.findById(trackId)
		  .orElseThrow(() -> new EntityNotFoundException(Track.class, Map.of("id", trackId)));

		playlist.addTrack(track);
		return playlistRepository.merge(playlist).getTracks();
	}

	@Override
	public List<Track> removeTrackFromPlaylist(int playlistId, int trackId) throws EntityNotFoundException {
		var playlist = playlistRepository
		  .findById(playlistId)
		  .orElseThrow(() -> new EntityNotFoundException(Playlist.class, Map.of("id", playlistId)));

		if (!playlist.removeTrack(trackId)) {
			throw new EntityNotFoundException(Track.class, Map.of("id", trackId));
		}

		return playlistRepository.merge(playlist).getTracks();
	}

	private static List<Track> toTracks(List<TrackRequest> trackRequests) {
		// This is a code smell, because the business layer shouldn't
		// know what information the persistence layer uses (i.e. 'ID only').
		// However, this prevents having to fetch unnecessarily tracks eagerly.
		return trackRequests.stream().map(tr -> {
			var track = new Track(null, null, 0, false, null);
			track.setId(tr.id());
			return track;
		}).toList();
	}

}
