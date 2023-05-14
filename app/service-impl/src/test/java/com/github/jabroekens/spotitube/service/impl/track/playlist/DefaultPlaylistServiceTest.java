package com.github.jabroekens.spotitube.service.impl.track.playlist;

import com.github.jabroekens.spotitube.model.Playlists;
import com.github.jabroekens.spotitube.model.Tracks;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
		var playlist = Playlists.EMPTY;
		var playlists = List.of(playlist);

		when(playlistRepository.findById(any())).thenReturn(Optional.empty());
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.createPlaylist(playlist);
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertIterableEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).save(playlist);
		verify(playlistRepository).findAll();
	}

	@Test
	void throwsExceptionWhenCreatingExistentPlaylist() {
		var playlist = Playlists.EMPTY;
		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));

		assertThrows(EntityExistsException.class, () -> sut.createPlaylist(playlist));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void getsAllPlaylistsAndHasCorrectLength() {
		var playlists = List.of(Playlists.EMPTY);
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.getAllPlaylists();
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertIterableEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).findAll();
	}

	@Test
	void modifiesPlaylistWhenExistent() {
		var playlist = Playlists.EMPTY;
		var playlists = List.of(playlist);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.modifyPlaylist(playlist);
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertIterableEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).save(playlist);
		verify(playlistRepository).findAll();
	}

	@Test
	void throwsExceptionWhenModifyingNonexistentPlaylist() {
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> sut.modifyPlaylist(Playlists.EMPTY));

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void removesPlaylistWhenExistent() {
		var playlist = Playlists.EMPTY;
		var playlists = List.of(playlist);

		when(playlistRepository.remove(any())).thenReturn(true);
		when(playlistRepository.findAll()).thenReturn(playlists);

		var playlistCollection = sut.removePlaylist(playlist.getId());
		assertAll(
		  () -> assertEquals(1, playlistCollection.length()),
		  () -> assertIterableEquals(playlists, playlistCollection.playlists())
		);

		verify(playlistRepository).remove(playlist.getId());
		verify(playlistRepository).findAll();
	}

	@Test
	void throwsExceptionWhenRemovingNonexistentPlaylist() {
		when(playlistRepository.remove(any())).thenReturn(false);
		assertThrows(EntityNotFoundException.class, () -> sut.removePlaylist(Playlists.EMPTY.getId()));
	}

	@Test
	void getsTracksForPlaylist() {
		var playlist = spy(Playlists.EMPTY);
		var tracks = List.of(Tracks.AMERICAN_LOVE);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(playlist.getTracks()).thenReturn(tracks);

		assertIterableEquals(tracks, sut.getPlaylistTracks(playlist.getId()));
	}

	@Test
	void throwsExceptionWhenGettingTracksForNonexistentPlaylist() {
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> sut.getPlaylistTracks(Playlists.EMPTY.getId()));
	}

	@Test
	void addsExistentTrackToExistentPlaylist() {
		var existingPlaylist = spy(Playlists.EMPTY);
		var track = Tracks.AMERICAN_LOVE;
		var modifiedPlaylist = spy(Playlists.EMPTY);
		var tracks = List.of(track);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(existingPlaylist));
		when(trackRepository.findById(any())).thenReturn(Optional.of(track));
		when(playlistRepository.save(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(tracks);

		assertIterableEquals(tracks, sut.addTrackToPlaylist(existingPlaylist.getId(), track.getId()));

		verify(existingPlaylist).addTrack(track);
		verify(playlistRepository).save(existingPlaylist);
		verify(modifiedPlaylist).getTracks();
	}

	@Test
	void throwsExceptionWhenAddingTrackToNonexistentPlaylist() {
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(
		  EntityNotFoundException.class,
		  () -> sut.addTrackToPlaylist(Playlists.EMPTY.getId(), Tracks.AMERICAN_LOVE.getId())
		);

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void throwsExceptionWhenAddingNonexistentTrackToPlaylist() {
		var playlist = Playlists.EMPTY;
		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(trackRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(
		  EntityNotFoundException.class,
		  () -> sut.addTrackToPlaylist(playlist.getId(), Tracks.AMERICAN_LOVE.getId())
		);

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void removesExistentTrackFromExistentPlaylist() {
		var existingPlaylist = spy(Playlists.EMPTY);
		var track = Tracks.AMERICAN_LOVE;
		var modifiedPlaylist = spy(Playlists.EMPTY);
		var tracks = List.of(track);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(existingPlaylist));
		when(existingPlaylist.removeTrack(any())).thenReturn(true);
		when(playlistRepository.save(any())).thenReturn(modifiedPlaylist);
		when(modifiedPlaylist.getTracks()).thenReturn(tracks);

		assertIterableEquals(tracks, sut.removeTrackFromPlaylist(existingPlaylist.getId(), track.getId()));

		verify(existingPlaylist).removeTrack(track.getId());
		verify(playlistRepository).save(existingPlaylist);
		verify(modifiedPlaylist).getTracks();
	}

	@Test
	void throwsExceptionWhenRemovingTrackFromNonexistentPlaylist() {
		when(playlistRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(
		  EntityNotFoundException.class,
		  () -> sut.removeTrackFromPlaylist(Playlists.EMPTY.getId(), Tracks.AMERICAN_LOVE.getId())
		);

		verifyNoMoreInteractions(playlistRepository);
	}

	@Test
	void throwsExceptionWhenRemovingNonexistentTrackFromPlaylist() {
		var playlist = spy(Playlists.EMPTY);

		when(playlistRepository.findById(any())).thenReturn(Optional.of(playlist));
		when(playlist.removeTrack(any())).thenReturn(false);

		assertThrows(
		  EntityNotFoundException.class,
		  () -> sut.removeTrackFromPlaylist(playlist.getId(), Tracks.AMERICAN_LOVE.getId())
		);

		verifyNoMoreInteractions(playlistRepository);
	}

}
