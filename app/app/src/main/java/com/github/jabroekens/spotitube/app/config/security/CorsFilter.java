package com.github.jabroekens.spotitube.app.config.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {


	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		var headers = responseContext.getHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
	}

}
