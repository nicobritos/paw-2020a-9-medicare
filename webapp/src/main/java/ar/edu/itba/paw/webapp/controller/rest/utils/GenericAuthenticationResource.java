package ar.edu.itba.paw.webapp.controller.rest.utils;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JWTAuthenticator;
import ar.edu.itba.paw.webapp.auth.UserRole;
import ar.edu.itba.paw.webapp.events.events.UserConfirmationTokenGenerationEvent;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class GenericAuthenticationResource extends GenericResource {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private JWTAuthenticator authenticator;
    @Autowired
    private UserService userService;

    protected boolean createJWTHeaders(ResponseBuilder responseBuilder, UserCredentials credentials, User user, HttpServletResponse response, Logger logger) {
        Collection<? extends GrantedAuthority> authorities;
        if (!user.getVerified()) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority(UserRole.UNVERIFIED.getAsRole()));
        } else {
            if (this.userService.isDoctor(user)) {
                authorities = Collections.singletonList(new SimpleGrantedAuthority(UserRole.DOCTOR.getAsRole()));
            } else {
                authorities = Collections.singletonList(new SimpleGrantedAuthority(UserRole.PATIENT.getAsRole()));
            }
        }

        Map<String, String> headers;
        try {
            Authentication authentication = this.authenticator.createAuthentication(credentials, authorities);
            headers = this.authenticator.createAndRefreshJWT(authentication, user);
        } catch (Exception e) {
            logger.error("Error creating JWT token for user id: {} with mail: {}", user.getId(), user.getEmail());
            return false;
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            responseBuilder.header(header.getKey(), header.getValue());
        }

        return true;
    }

    protected void sendConfirmationEmail(HttpServletRequest request, User newUser) {
        StringBuilder baseUrl = new StringBuilder(request.getRequestURL());
        baseUrl.replace(request.getRequestURL().lastIndexOf(request.getServletPath()), request.getRequestURL().length(), "");
        this.eventPublisher.publishEvent(new UserConfirmationTokenGenerationEvent(baseUrl.toString(), newUser, "/verify", request.getLocale()));
    }
}
