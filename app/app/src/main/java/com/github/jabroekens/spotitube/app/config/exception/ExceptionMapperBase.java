package com.github.jabroekens.spotitube.app.config.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public abstract class ExceptionMapperBase<T extends Throwable> implements ExceptionMapper<T> {

	protected abstract Response.Status getStatus();

	protected Response.ResponseBuilder decorate(T t, Response.ResponseBuilder responseBuilder) {
		return responseBuilder;
	}

	@Override
	public final Response toResponse(T exception) {
		var responseBuilder = Response
		  .status(getStatus())
		  .entity(new ExceptionResponse(exception.getMessage()));

		return decorate(exception, responseBuilder).build();
	}

}
