package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Tracks;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackRepositoryTest extends TestBase {

	@InjectMocks
	private JdbcTrackRepository sut;

	public Stream<Arguments> sqlExceptionThrowingMethods() {
		return Stream.of(
		  Arguments.of((Executable) () -> sut.findAll()),
		  Arguments.of((Executable) () -> sut.findById(1)),
		  Arguments.of((Executable) () -> sut.add(Tracks.DearNia())),
		  Arguments.of((Executable) () -> sut.merge(Tracks.AmericanLove())),
		  Arguments.of((Executable) () -> sut.remove(1))
		);
	}

}
