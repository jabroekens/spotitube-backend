package com.github.jabroekens.spotitube.service.api;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends NoSuchElementException {

	private static final String ERROR_MSG = "Entity of type '%s' with details '%s' has not been found.";

	private final Class<?> entityType;
	private final String entityId;

	public EntityNotFoundException(Class<?> entityType, Object entityId) {
		this(entityType, entityId, null);
	}

	public EntityNotFoundException(Class<?> entityType, Object entityId, Throwable cause) {
		super(ERROR_MSG.formatted(entityType.getSimpleName(), entityId), cause);
		this.entityType = entityType;
		this.entityId = entityId.toString();
	}

	public Class<?> getEntityType() {
		return entityType;
	}

	public String getEntityId() {
		return entityId;
	}

}
