package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.model.Users;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
class DefaultPlaylistServiceTest {

	@Mock
	private PlaylistRepository playlistRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TrackRepository trackRepository;

	@InjectMocks
	private DefaultPlaylistService sut;

	@Test
	void createsPlaylistWhenNonexistent() {
		var playlist = Playlists.Empty();
		when(userRepository.findById(any())).thenReturn(Optional.of(playlist.getOwner()));
		when(playlistRepository.add(any())).thenReturn(playlist);

		var createdPlaylist = sut.createPlaylist(toPlaylistRequest(playlist));

		assertEquals(playlist, createdPlaylist);
		verify(playlistRepository).add(playlist);
	}

	@Test
	void throwsExceptionWhenCreatingPlaylistWithNonexistentOwner() {
		var playlistRequest = toPlaylistRequest(Playlists.Empty());
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> sut.createPlaylist(playlistRequest));
	}

	@Test
	void throwsExceptionWhenCreatingExistentPlaylist() {
		var playlistRequest = toPlaylistRequest(Playlists.Empty());
		when(userRepository.findById(any())).thenReturn(Optional.of(Users.JohnDoe()));
		when(playlistRepository.add(any())).thenThrow(PersistenceException.class);

		assertThrows(EntityExistsException.class, () -> sut.createPlaylist(playlistRequest));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void getsAllPlaylistsAndHasCorrectLength() {
		var playlists = List.of(Playlists.Empty());
		when(playlistRepository.findAll()).thenReturn(playlists);

		var foundPlaylists = sut.getAllPlaylists();

		assertIterableEquals(playlists, foundPlaylists);
		verify(playlistRepository).findAll();
	}

	@Test
	void modifiesPlaylistWhenExistent() {
		var playlist = Playlists.Empty();
		when(userRepository.findById(any())).thenReturn(Optional.of(playlist.getOwner()));
		when(playlistRepository.merge(any())).thenReturn(playlist);

		var modifiedPlaylist = sut.modifyPlaylist(toPlaylistRequest(playlist));

		assertEquals(playlist, modifiedPlaylist);
		verify(playlistRepository).merge(playlist);
	}

	@Test
	void throwsExceptionWhenModifyingPlaylistWithNonexistentOwner() {
		var playlistRequest = toPlaylistRequest(Playlists.Empty());
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> sut.modifyPlaylist(playlistRequest));
	}

	@Test
	void throwsExceptionWhenModifyingNonexistentPlaylist() {
		var playlistRequest = toPlaylistRequest(Playlists.Empty());
		when(userRepository.findById(any())).thenReturn(Optional.of(Users.JohnDoe()));
		when(playlistRepository.merge(any())).thenThrow(PersistenceException.class);

		assertThrows(EntityNotFoundException.class, () -> sut.modifyPlaylist(playlistRequest));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void removesPlaylistWhenExistent() {
		var playlistId = Playlists.Empty().getId().get();
		when(playlistRepository.remove(any())).thenReturn(true);

		assertDoesNotThrow(() -> sut.removePlaylist(playlistId));

		verify(playlistRepository).remove(playlistId);
	}

	@Test
	void throwsExceptionWhenRemovingNonexistentPlaylist() {
		var playlistId = Playlists.Empty().getId().get();
		when(playlistRepository.remove(any())).thenReturn(false);
		assertThrows(EntityNotFoundException.class, () -> sut.removePlaylist(playlistId));
	}

	@Test
	void getsTracksForPlaylist() {
		var playlist = spy(Playlists.Empty());
		var playlistId = playlist.getId().get();
		var tracks = List.of(Tracks.AmericanLove());

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(playlist.getTracks()).thenReturn(tracks);

		assertIterableEquals(tracks, sut.getPlaylistTracks(playlistId));
	}

	@Test
	void throwsExceptionWhenGettingTracksForNonexistentPlaylist() {
		var playlistId = Playlists.Empty().getId().get();
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> sut.getPlaylistTracks(playlistId));
	}

	@Test
	void addsExistentTrackToExistentPlaylist() {
		var existingPlaylist = spy(Playlists.Empty());
		var existingPlaylistId = existingPlaylist.getId().get();

		var track = Tracks.AmericanLove();
		var trackId = track.getId().get();

		var modifiedPlaylist = spy(Playlists.Empty());
		var expectedTracks = List.of(track);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(existingPlaylist));
		when(trackRepository.findById(any())).thenReturn(Optional.of(track));
		when(playlistRepository.merge(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(expectedTracks);

		assertIterableEquals(expectedTracks, sut.addTrackToPlaylist(existingPlaylistId, trackId));

		verify(existingPlaylist).addTrack(track);
		verify(playlistRepository).merge(existingPlaylist);
		verify(modifiedPlaylist).getTracks();
	}

	@Test
	void throwsExceptionWhenAddingTrackToNonexistentPlaylist() {
		var playlistId = Playlists.Empty().getId().get();
		var trackId = Tracks.AmericanLove().getId().get();

		when(playlistRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> sut.addTrackToPlaylist(playlistId, trackId));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void throwsExceptionWhenAddingNonexistentTrackToPlaylist() {
		var playlist = Playlists.Empty();
		var playlistId = playlist.getId().get();
		var trackId = Tracks.AmericanLove().getId().get();

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(trackRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> sut.addTrackToPlaylist(playlistId, trackId));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void removesExistentTrackFromExistentPlaylist() {
		var existingPlaylist = spy(Playlists.Empty());
		var existingPlaylistId = existingPlaylist.getId().get();

		var track = Tracks.AmericanLove();
		int trackId = track.getId().get();

		var modifiedPlaylist = spy(Playlists.Empty());
		var expectedTracks = List.of(track);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(existingPlaylist));
		when(existingPlaylist.removeTrack(anyInt())).thenReturn(true);
		when(playlistRepository.merge(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(expectedTracks);

		assertIterableEquals(expectedTracks, sut.removeTrackFromPlaylist(existingPlaylistId, trackId));

		verify(existingPlaylist).removeTrack(trackId);
		verify(playlistRepository).merge(existingPlaylist);
		verify(modifiedPlaylist).getTracks();
	}

	@Test
	void throwsExceptionWhenRemovingTrackFromNonexistentPlaylist() {
		var playlistId = Playlists.Empty().getId().get();
		var trackId = Tracks.AmericanLove().getId().get();
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> sut.removeTrackFromPlaylist(playlistId, trackId));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void throwsExceptionWhenRemovingNonexistentTrackFromPlaylist() {
		var playlist = spy(Playlists.Empty());
		var playlistId = playlist.getId().get();
		var trackId = Tracks.AmericanLove().getId().get();

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(playlist.removeTrack(anyInt())).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> sut.removeTrackFromPlaylist(playlistId, trackId));

		verifyNoMoreInteractions(playlistRepository);
	}

	private static PlaylistRequest toPlaylistRequest(Playlist playlist) {
		return new PlaylistRequest(
		  playlist.getId().orElse(-1),
		  playlist.getName(),
		  playlist.getOwner().getId(),
		  playlist.getTracks()
		);
	}

}
