package com.github.jabroekens.spotitube.app.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Strings;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.security.Principal;
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
			var claims = JwtHelper.validateToken(token.get());
			var securityContext = buildSecurityContext(requestContext.getSecurityContext(), claims);
			requestContext.setSecurityContext(securityContext);
		} catch (JwtException e) {
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
		return Optional.ofNullable(requestContext.getUriInfo().getQueryParameters().getFirst(TOKEN_PARAM));
	}

	private SecurityContext buildSecurityContext(SecurityContext currentSecurityContext, Claims claims) {
		return new SecurityContext() {
			@Override
			public Principal getUserPrincipal() {
				return claims::getSubject;
			}

			@Override
			public boolean isUserInRole(String role) {
				return true;
			}

			@Override
			public boolean isSecure() {
				return currentSecurityContext.isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return AUTH_SCHEME;
			}
		};
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
