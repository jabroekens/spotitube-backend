package com.github.jabroekens.spotitube.app.resource.track;

import com.github.jabroekens.spotitube.app.config.security.Secured;
import com.github.jabroekens.spotitube.app.resource.track.dto.FilteredTracksResponse;
import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.service.api.track.TrackService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Secured
@Path("/tracks")
@Produces(MediaType.APPLICATION_JSON)
public class TrackResource {

	@Inject
	private TrackService trackService;

	@GET
	public Response getAvailableTracks(@QueryParam("forPlaylist") Integer playlistId) {
		List<Track> tracks;
		if (playlistId != null) {
			tracks = trackService.getAvailableTracks(playlistId);
		} else {
			tracks = trackService.getAvailableTracks();
		}

		return Response.ok(new FilteredTracksResponse(tracks)).build();
	}

}
