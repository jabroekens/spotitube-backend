package com.github.jabroekens.spotitube.app.config.exception;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
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
		var entityType = exception.getEntityType();
		var entityId = exception.getEntityId();

		if (User.class.isAssignableFrom(entityType)) {
			return "/users/" + entityId;
		} else if (Playlist.class.isAssignableFrom(entityType)) {
			return "/playlists/" + entityId;
		} else if (Track.class.isAssignableFrom(entityType)) {
			return "/tracks/" + entityId;
		} else {
			return "/";
		}
	}

}
