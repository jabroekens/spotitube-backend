package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Playlists;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("MagicConstant")
@ExtendWith(MockitoExtension.class)
class PlaylistRepositoryTest extends TestBase {

	@InjectMocks
	private JdbcPlaylistRepository sut;

	public Stream<Arguments> sqlExceptionThrowingMethods() {
		return Stream.of(
		  Arguments.of((Executable) () -> sut.findAll()),
		  Arguments.of((Executable) () -> sut.findById(1)),
		  Arguments.of((Executable) () -> sut.add(Playlists.Videos())),
		  Arguments.of((Executable) () -> sut.merge(Playlists.Favorites())),
		  Arguments.of((Executable) () -> sut.remove(1))
		);
	}

	@Test
	void rollsBackIfErrorOccursWhenAdding(@Mock Connection conn) throws SQLException {
		when(super.dataSource.getConnection()).thenReturn(conn);
		when(conn.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
		sut.add(Playlists.Videos());
		verify(conn).rollback();
	}

	@Test
	void rollsBackIfErrorOccursWhenMerging(@Mock Connection conn) throws SQLException {
		when(dataSource.getConnection()).thenReturn(conn);
		when(conn.prepareStatement(anyString())).thenThrow(SQLException.class);
		sut.merge(Playlists.Empty());
		verify(conn).rollback();
	}

}
