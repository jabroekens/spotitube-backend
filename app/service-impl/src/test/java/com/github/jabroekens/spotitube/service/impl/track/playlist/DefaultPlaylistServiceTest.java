package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
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
	private TrackRepository trackRepository;

	@InjectMocks
	private DefaultPlaylistService sut;

	@Test
	void createsPlaylistWhenNonexistent() {
		var playlist = Playlists.Empty();
		var playlists = List.of(playlist);

		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.createPlaylist(playlist);
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).add(playlist);
		verify(playlistRepository).findAll();
	}

	@Test
	void throwsExceptionWhenCreatingExistentPlaylist() {
		when(playlistRepository.add(any())).thenThrow(PersistenceException.class);
		assertThrows(EntityExistsException.class, () -> sut.createPlaylist(Playlists.Empty()));
		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void getsAllPlaylistsAndHasCorrectLength() {
		var playlists = List.of(Playlists.Empty());
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.getAllPlaylists();
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).findAll();
	}

	@Test
	void modifiesPlaylistWhenExistent() {
		var playlist = Playlists.Empty();
		var playlists = List.of(playlist);

		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.modifyPlaylist(playlist);
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).merge(playlist);
		verify(playlistRepository).findAll();
	}

	@Test
	void throwsExceptionWhenModifyingNonexistentPlaylist() {
		when(playlistRepository.merge(any())).thenThrow(PersistenceException.class);
		assertThrows(EntityNotFoundException.class, () -> sut.modifyPlaylist(Playlists.Empty()));
		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void removesPlaylistWhenExistent() {
		var playlist = Playlists.Empty();
		var playlistId = playlist.getId().get();
		var playlists = List.of(playlist);

		when(playlistRepository.remove(any())).thenReturn(true);
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.removePlaylist(playlistId);
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).remove(playlistId);
		verify(playlistRepository).findAll();
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
		when(playlistRepository.add(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(expectedTracks);

		assertIterableEquals(expectedTracks, sut.addTrackToPlaylist(existingPlaylistId, trackId));

		verify(existingPlaylist).addTrack(track);
		verify(playlistRepository).add(existingPlaylist);
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
		when(playlistRepository.add(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(expectedTracks);

		assertIterableEquals(expectedTracks, sut.removeTrackFromPlaylist(existingPlaylistId, trackId));

		verify(existingPlaylist).removeTrack(trackId);
		verify(playlistRepository).add(existingPlaylist);
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

}
