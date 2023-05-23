package com.github.jabroekens.spotitube.app.config.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionMapper extends ExceptionMapperBase<RuntimeException> {

	@Override
	protected Response.Status getStatus() {
		return Response.Status.INTERNAL_SERVER_ERROR;
	}

}
