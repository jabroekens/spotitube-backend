package com.github.jabroekens.spotitube.app.config.exception;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.service.api.EntityExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.net.URI;

@Provider
public class EntityExistsExceptionMapper extends ExceptionMapperBase<EntityExistsException> {

	@Override
	protected Response.Status getStatus() {
		return Response.Status.SEE_OTHER;
	}

	@Override
	protected Response.ResponseBuilder decorate(
	  EntityExistsException exception,
	  Response.ResponseBuilder responseBuilder
	) {
		return responseBuilder.location(URI.create(determineLocation(exception)));
	}

	private String determineLocation(EntityExistsException exception) {
		if (exception.getEntityType().equals(User.class)) {
			return "/user/" + exception.getEntityId();
		} else {
			return "/";
		}
	}

}
