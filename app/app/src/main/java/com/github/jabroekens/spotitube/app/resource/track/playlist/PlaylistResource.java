package com.github.jabroekens.spotitube.app.resource.track.playlist;

import com.github.jabroekens.spotitube.app.config.security.Secured;
import com.github.jabroekens.spotitube.app.resource.track.dto.FilteredTrackRequest;
import com.github.jabroekens.spotitube.app.resource.track.dto.FilteredTracksResponse;
import com.github.jabroekens.spotitube.app.resource.track.playlist.dto.FilteredPlaylistRequest;
import com.github.jabroekens.spotitube.app.resource.track.playlist.dto.FilteredPlaylistsResponse;
import com.github.jabroekens.spotitube.service.api.track.TrackRequest;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest;
import com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

@Secured
@Path("/playlists")
@Produces(MediaType.APPLICATION_JSON)
public class PlaylistResource {

    @Inject
    private PlaylistService playlistService;

    @Context
    private SecurityContext securityContext;

    @GET
    public Response getAllPlaylists() {
        var authenticatedUser = securityContext.getUserPrincipal().getName();
        var playlistsResponse = new FilteredPlaylistsResponse(
          playlistService.getAllPlaylists(),
          authenticatedUser
        );
        return Response.ok(playlistsResponse).build();
    }

    @DELETE
    @Path("{id}")
    public Response removePlaylist(@PathParam("id") int playlistId) {
        playlistService.removePlaylist(playlistId);
        return getAllPlaylists();
    }

    @POST
    public Response addPlaylist(FilteredPlaylistRequest filteredPlaylistRequest) {
        playlistService.createPlaylist(toPlaylistRequest(filteredPlaylistRequest));
        return getAllPlaylists();
    }

    @PUT
    @Path("{id}")
    public Response editPlaylist(@PathParam("id") int playlistId, FilteredPlaylistRequest filteredPlaylistRequest) {
        // TODO verify playlistId matches?
        playlistService.modifyPlaylist(toPlaylistRequest(filteredPlaylistRequest));
        return getAllPlaylists();
    }

    @GET
    @Path("{id}/tracks")
    public Response getPlaylistTracks(@PathParam("id") int playlistId) {
        var tracks = playlistService.getPlaylistTracks(playlistId);
        return Response.ok(new FilteredTracksResponse(tracks)).build();
    }

    @POST
    @Path("{id}/tracks")
    public Response addPlaylistTrack(@PathParam("id") int playlistId, FilteredTrackRequest filteredTrackRequest) {
        playlistService.addTrackToPlaylist(playlistId, filteredTrackRequest.id());
        return getPlaylistTracks(playlistId);
    }

    @DELETE
    @Path("{id}/tracks/{trackId}")
    public Response removePlaylistTrack(@PathParam("id") int playlistId, @PathParam("trackId") int trackId) {
        playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return getPlaylistTracks(playlistId);
    }

    private PlaylistRequest toPlaylistRequest(FilteredPlaylistRequest filteredPlaylistRequest) {
        var authenticatedUser = securityContext.getUserPrincipal().getName();

        // Ignore FilteredPlaylistRequest's `owner` value; force the
        // playlist-to-be-made's owner to be the authenticated user as
        // required by our domain model (a playlist must have an owner)
        return new PlaylistRequest(
          filteredPlaylistRequest.id(),
          filteredPlaylistRequest.name(),
          authenticatedUser,
          Optional.ofNullable(filteredPlaylistRequest.tracks()).orElse(List.of())
            .stream().map(PlaylistResource::toTrackRequest).toList()
        );
    }

    private static TrackRequest toTrackRequest(FilteredTrackRequest filteredTrackRequest) {
        // Ignore FilteredTrackRequest's `performer` value; that's the names not the ID.
        return new TrackRequest(
          filteredTrackRequest.id(),
          filteredTrackRequest.title(),
          null,
          filteredTrackRequest.duration(),
          filteredTrackRequest.offlineAvailable(),
          filteredTrackRequest.album(),
          filteredTrackRequest.playCount(),
          filteredTrackRequest.publicationDate(),
          filteredTrackRequest.description()
        );
    }

}
