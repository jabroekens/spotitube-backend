package com.github.jabroekens.spotitube.app.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Strings;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.security.KeyStoreException;
import java.util.Optional;

@Secured
@Provider
public class AuthFilter implements ContainerRequestFilter {

	private static final String AUTH_SCHEME = "Bearer";
	private static final String TOKEN_PARAM = "token";

	@Override
	public void filter(ContainerRequestContext requestContext) {
		var token = extractTokenFromHeader(requestContext)
		  .or(() -> extractTokenFromQueryParams(requestContext));

		if (token.isEmpty()) {
			abortWithUnauthorized(requestContext);
			return;
		}

		try {
			JwtUtil.validateToken(token.get());
		} catch (JwtException | KeyStoreException e) {
			abortWithUnauthorized(requestContext);
		}
	}

	private Optional<String> extractTokenFromHeader(ContainerRequestContext requestContext) {
		var authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (!Strings.startsWithIgnoreCase(authHeader, AUTH_SCHEME + " ")) {
			return Optional.empty();
		}

		return Optional.of(authHeader.substring(AUTH_SCHEME.length()).trim());
	}

	private Optional<String> extractTokenFromQueryParams(ContainerRequestContext requestContext) {
		return Optional.of(requestContext.getUriInfo().getQueryParameters().getFirst(TOKEN_PARAM));
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
