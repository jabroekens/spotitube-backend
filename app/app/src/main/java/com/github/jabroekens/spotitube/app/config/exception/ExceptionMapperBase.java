package com.github.jabroekens.spotitube.app.config.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public abstract class ExceptionMapperBase<T extends Throwable> implements ExceptionMapper<T> {

	protected abstract Response.Status getStatus();

	/**
	 * Decorates the response to be built for {@link T}.
	 *
	 * @param t               the throwable for which the response will be built.
	 * @param responseBuilder the response builder to be decorated.
	 *
	 * @return {@code responseBuilder}.
	 */
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
