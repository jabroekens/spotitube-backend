package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Users;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends TestBase {

	@InjectMocks
	private JdbcUserRepository sut;

	public Stream<Arguments> sqlExceptionThrowingMethods() {
		return Stream.of(
		  Arguments.of((Executable) () -> sut.findAll()),
		  Arguments.of((Executable) () -> sut.findById("john")),
		  Arguments.of((Executable) () -> sut.findByName("John Doe")),
		  Arguments.of((Executable) () -> sut.add(Users.JaneDoe())),
		  Arguments.of((Executable) () -> sut.merge(Users.JohnDoe())),
		  Arguments.of((Executable) () -> sut.remove("john"))
		);
	}

}
