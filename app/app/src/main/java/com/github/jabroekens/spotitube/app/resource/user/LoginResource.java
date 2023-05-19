package com.github.jabroekens.spotitube.app.resource.user;

import com.github.jabroekens.spotitube.app.config.security.JwtHelper;
import com.github.jabroekens.spotitube.service.api.user.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @Inject
    private UserService userService;

    @POST
    public Response login(LoginRequest loginRequest) {
        var user = userService.getUser(loginRequest.user(), loginRequest.password());
        var token = JwtHelper.issueToken(user);
        return Response.ok(new LoginResponse(token, user.getName())).build();
    }

}
