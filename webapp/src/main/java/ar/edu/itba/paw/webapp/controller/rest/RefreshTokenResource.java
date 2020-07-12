package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.RefreshToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericAuthenticationResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("/refresh")
@Component
public class RefreshTokenResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenResource.class);

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @POST
    @Path("{token}")
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpServletResponse response,
            @Context HttpHeaders httpheaders,
            @PathParam("token") String token)
    {
        if (token == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<RefreshToken> refreshTokenOptional = this.refreshTokenService.findByToken(token);
        if (!refreshTokenOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        Optional<User> userOptional = this.userService.findByRefreshTokenId(refreshTokenOptional.get().getId());
        if (!userOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(userOptional.get().getEmail());
        userCredentials.setPassword(token);

        ResponseBuilder responseBuilder = Response.ok();
        this.createJWTHeaders(responseBuilder, userCredentials, userOptional.get(), response, LOGGER);

        return responseBuilder.build();
    }
}