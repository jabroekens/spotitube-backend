package com.github.jabroekens.spotitube.service.api;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {

	private static final String ERROR_MSG = "Entity of type '%s' with ID '%s' has not been found.";

	private final Class<?> entityType;
	private final String entityId;

	public EntityNotFoundException(Class<?> entityType, String entityId) {
		this(entityType, entityId, null);
	}

	public EntityNotFoundException(Class<?> entityType, String entityId, Throwable cause) {
		super(ERROR_MSG.formatted(entityType.getSimpleName(), entityId));
		this.entityType = entityType;
		this.entityId = entityId;
	}

	public Class<?> getEntityType() {
		return entityType;
	}

	public String getEntityId() {
		return entityId;
	}

}
