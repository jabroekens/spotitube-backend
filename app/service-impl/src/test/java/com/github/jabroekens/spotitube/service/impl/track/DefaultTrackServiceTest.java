package com.github.jabroekens.spotitube.service.impl.track;

import com.github.jabroekens.spotitube.model.Tracks;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTrackServiceTest {

	@Mock
	private TrackRepository trackRepository;

	@Mock
	private PlaylistService playlistService;

	@InjectMocks
	private DefaultTrackService sut;

	@Test
	void getsAllAvailableTracks() {
		var tracks = Stream.of(
		  Tracks.AmericanLove(), Tracks.TheEgg()
		).collect(Collectors.toCollection(ArrayList::new));

		when(trackRepository.findAll()).thenReturn(tracks);

		assertIterableEquals(tracks, sut.getAvailableTracks());

		verify(trackRepository).findAll();
	}

	@Test
	void getsAllAvailableTracksForPlaylist() {
		var tracks = List.of(Tracks.AmericanLove(), Tracks.TheEgg());
		var playlistTracks = List.of(tracks.get(0));
		var expectedTracks = List.of(tracks.get(1));
		var playlistId = 1;

		when(playlistService.getPlaylistTracks(anyInt())).thenReturn(playlistTracks);
		when(trackRepository.findAll()).thenReturn(tracks);

		assertIterableEquals(expectedTracks, sut.getAvailableTracks(playlistId));

		verify(playlistService).getPlaylistTracks(playlistId);
		verify(trackRepository).findAll();
	}

}
