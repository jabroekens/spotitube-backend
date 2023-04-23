package com.github.jabroekens.spotitube.app.config.exception;

import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper extends ExceptionMapperBase<EntityNotFoundException> {

	@Override
	protected Response.Status getStatus() {
		return Response.Status.NOT_FOUND;
	}

}
