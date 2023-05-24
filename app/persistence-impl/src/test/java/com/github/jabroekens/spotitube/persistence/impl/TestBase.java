package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import java.sql.SQLException;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public abstract class TestBase {

	@Mock
	protected DataSource dataSource;

	@ParameterizedTest
	@MethodSource("sqlExceptionThrowingMethods")
	void wrapsSQLExceptionInPersistenceException(Executable sqlThrowingMethod) throws SQLException {
		when(dataSource.getConnection()).thenThrow(SQLException.class);
		assertThrows(PersistenceException.class, sqlThrowingMethod);
	}

	public abstract Stream<Arguments> sqlExceptionThrowingMethods();

}
