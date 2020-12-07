package ar.edu.itba.paw.webapp.controller.rest;

import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.RefreshToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.Constants;
import ar.edu.itba.paw.webapp.controller.rest.utils.GenericAuthenticationResource;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.Optional;

@Path("/" + Constants.REFRESH_TOKEN_ENDPOINT)
@Component
public class RefreshTokenResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenResource.class);

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @POST
    @Produces({MediaType.WILDCARD, ErrorMIME.ERROR})
    public Response getEntity(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response)
    {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(Constants.REFRESH_TOKEN_COOKIEN_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (token == null)
            return this.error(Status.BAD_REQUEST.getStatusCode(), Status.BAD_REQUEST.toString());

        Optional<RefreshToken> refreshTokenOptional = this.refreshTokenService.findByToken(token);
        if (!refreshTokenOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());

        Optional<User> userOptional = this.userService.findByRefreshTokenId(refreshTokenOptional.get().getId());
        if (!userOptional.isPresent())
            return this.error(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.toString());
        if (userOptional.get().getRefreshToken().getCreatedDate().plusMillis((int) Constants.JWT_REFRESH_EXPIRATION_MILLIS).isBeforeNow())
            return this.error(Status.UNAUTHORIZED.getStatusCode(), Status.UNAUTHORIZED.toString());

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(userOptional.get().getEmail());
        userCredentials.setPassword(token);

        if (!this.createJWTCookies(userCredentials, userOptional.get(), response, LOGGER)) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Status.NO_CONTENT).build();
    }
}