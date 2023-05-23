package com.github.jabroekens.spotitube.service.api;

import java.util.Map;
import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {

	private static final String ERROR_MSG = "Entity of type '%s' with details '%s' has not been found.";

	public EntityNotFoundException(Class<?> entityType, Map<String, Object> details) {
		this(entityType, null, details);
	}

	public EntityNotFoundException(Class<?> entityType, Throwable cause, Map<String, Object> details) {
		super(ERROR_MSG.formatted(entityType.getSimpleName(), formatDetails(details)), cause);
	}

	private static String formatDetails(Map<String, Object> details) {
		// Sort to guarantee predictable log messages (and thus stable tests)
		return details.entrySet().stream().sorted(Map.Entry.comparingByKey())
		  .map(e -> "%s=%s".formatted(e.getKey(), e.getValue()))
		  .reduce((i, j) -> String.join(", ", i, j)).orElse("");
	}

}
