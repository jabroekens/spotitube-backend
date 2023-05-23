package com.github.jabroekens.spotitube.service.api;

import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityNotFoundExceptionTest {

	@Test
	void formatsDetailsCorrectly() {
		var expectedMessage = "Entity of type 'Object' with details 'bar=baz, id=1, name=foo' has not been found.";
		var exception = new EntityNotFoundException(Object.class, Map.of("id", "1", "name", "foo", "bar", "baz"));
		assertEquals(expectedMessage, exception.getMessage());
	}

}
