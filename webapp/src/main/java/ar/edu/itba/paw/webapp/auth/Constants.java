package ar.edu.itba.paw.webapp.auth;

import java.util.concurrent.TimeUnit;

public abstract class Constants {
    public static final String JWT_CLAIMS_ROLE = "role";
    public static final String JWT_CLAIMS_USERNAME = "username";
    public static final long JWT_EXPIRATION_MILLIS = TimeUnit.HOURS.toMillis(1);
    public static final long JWT_REFRESH_EXPIRATION_MILLIS = TimeUnit.DAYS.toMillis(7);
    public static final String JWT_COOKIE_NAME = "X-Jwt";
    public static final String REFRESH_TOKEN_COOKIEN_NAME = "X-Refresh-Token";
    public static final String EMPTY_COOKIE = "";
    public static final String REFRESH_TOKEN_ENDPOINT = "refresh";
    public static final String LOGOUT_ENDPOINT = "logout";
    public static final String AUTH_ENDPOINT = "auth";
}
