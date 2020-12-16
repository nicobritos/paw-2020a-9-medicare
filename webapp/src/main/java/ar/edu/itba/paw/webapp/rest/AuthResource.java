package ar.edu.itba.paw.webapp.rest;

import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.RefreshToken;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.UserMIME;
import ar.edu.itba.paw.webapp.models.Constants;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import ar.edu.itba.paw.webapp.models.UserMe;
import ar.edu.itba.paw.webapp.models.UserMeFactory;
import ar.edu.itba.paw.webapp.rest.utils.GenericAuthenticationResource;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Path("/" + Constants.AUTH_ENDPOINT)
@Component
public class AuthResource extends GenericAuthenticationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;

    @POST
    @Produces({UserMIME.ME, ErrorMIME.ERROR})
    @Path("/" + Constants.REFRESH_TOKEN_ENDPOINT)
    public Response refreshToken(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {
        String token = this.getRefreshToken(request);

        if (token == null)
            throw this.missingPathParams();

        Optional<RefreshToken> refreshTokenOptional = this.refreshTokenService.findByToken(token);
        if (!refreshTokenOptional.isPresent())
            throw this.notFound();

        Optional<User> userOptional = this.userService.findByRefreshTokenId(refreshTokenOptional.get().getId());
        if (!userOptional.isPresent())
            throw this.notFound();
        if (userOptional.get().getRefreshToken().getCreatedDate().plusMillis((int) Constants.JWT_REFRESH_EXPIRATION_MILLIS).toDateTime().isBeforeNow())
            throw this.unauthorized();

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(userOptional.get().getEmail());
        userCredentials.setPassword(token);

        if (!this.createJWTCookies(userCredentials, userOptional.get(), response, LOGGER)) {
            throw this.error(Status.INTERNAL_SERVER_ERROR).getError();
        }

        UserMe userMe;
        Collection<Doctor> doctors = this.doctorService.findByUser(userOptional.get());
        if (doctors.size() == 0) {
            userMe = UserMeFactory.withPatients(userOptional.get(), this.patientService.findByUser(userOptional.get()));
        } else {
            userMe = UserMeFactory.withDoctors(userOptional.get(), doctors);
        }

        return Response
                .status(Status.OK)
                .entity(userMe)
                .type(UserMIME.ME)
                .build();
    }

    @POST
    @Produces({ErrorMIME.ERROR})
    @Path("/" + Constants.LOGOUT_ENDPOINT)
    public Response invalidateToken(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {
        String token = this.getRefreshToken(request);
        if (token != null) this.refreshTokenService.removeByToken(token);

        this.authenticator.invalidateJWTCookies(response);

        return Response
                .status(Status.NO_CONTENT)
                .build();
    }

    private String getRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(Constants.REFRESH_TOKEN_COOKIEN_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}