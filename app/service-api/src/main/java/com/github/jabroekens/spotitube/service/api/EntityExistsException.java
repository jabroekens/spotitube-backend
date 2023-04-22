package com.github.jabroekens.spotitube.service.api;

public class EntityExistsException extends IllegalStateException {

	private static final String ERROR_MSG = "Entity of type '%s' with ID '%s' already exists.";

	private final Class<?> entityType;
	private final String entityId;

	public EntityExistsException(Class<?> entityType, String entityId) {
		this(entityType, entityId, null);
	}

	public EntityExistsException(Class<?> entityType, String entityId, Throwable cause) {
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
