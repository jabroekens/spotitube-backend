package com.github.jabroekens.spotitube.app.resource.user;

import com.github.jabroekens.spotitube.app.config.exception.ExceptionMapperBase;
import com.github.jabroekens.spotitube.service.api.user.IncorrectPasswordException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IncorrectPasswordExceptionMapper extends ExceptionMapperBase<IncorrectPasswordException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.UNAUTHORIZED;
    }

}
