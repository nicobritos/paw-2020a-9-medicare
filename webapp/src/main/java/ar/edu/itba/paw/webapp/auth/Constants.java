package ar.edu.itba.paw.webapp.auth;

import java.util.concurrent.TimeUnit;

public abstract class Constants {
    public static final String JWT_CLAIMS_ROLE = "role";
    public static final String JWT_CLAIMS_USERNAME = "username";
    public static final long JWT_EXPIRATION_TIME = TimeUnit.HOURS.toMillis(1);
    public static final long JWT_REFRESH_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7);
    public static final String AUTHORIZATION_REFRESH_HEADER = "Refresh-Token";

    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    public static final String ISSUER_INFO = "https://pawserver.it.itba.edu.ar/paw-2020a-9/";
}
