package com.github.jabroekens.spotitube.app.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Strings;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.security.KeyStoreException;

@Secured
@Provider
public class AuthFilter implements ContainerRequestFilter {

	private static final String AUTH_SCHEME = "Bearer";

	@Override
	public void filter(ContainerRequestContext requestContext) {
		var authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		var token = extractToken(authHeader);

		if (token == null) {
			abortWithUnauthorized(requestContext);
			return;
		}

		try {
			JwtUtil.validateToken(token);
		} catch (JwtException | KeyStoreException e) {
			abortWithUnauthorized(requestContext);
		}
	}

	private String extractToken(String authHeader) {
		if (!Strings.startsWithIgnoreCase(authHeader, AUTH_SCHEME + " ")) {
			return null;
		}

		return authHeader.substring(AUTH_SCHEME.length()).trim();
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
		requestContext.abortWith(
		  Response
			.status(Response.Status.UNAUTHORIZED)
			.header(HttpHeaders.WWW_AUTHENTICATE, "%s realm=\"%s\"".formatted(AUTH_SCHEME, "spotitube"))
			.build()
		);
	}

}
