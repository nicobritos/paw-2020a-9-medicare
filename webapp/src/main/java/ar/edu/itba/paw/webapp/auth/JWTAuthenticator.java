package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import ar.edu.itba.paw.webapp.utils.CookieUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@PropertySource("classpath:application-prod.properties")
@PropertySource("classpath:application-local.properties") // This will precede previous properties
public class JWTAuthenticator {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @Value("${app.api.path}")
    private String APP_SUBPATH;
    @Value("${app.host}")
    private String APP_HOST;

    private final String secret;

    public JWTAuthenticator() throws IOException {
        this.secret = this.getSecretKey();
    }

    public Authentication attemptAuthentication(UserCredentials credentials) throws AuthenticationException {
        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword(),
                        new LinkedList<>()
                ));
    }

    public Authentication createAuthentication(UserCredentials credentials, Collection<? extends GrantedAuthority> authorities) throws AuthenticationException {
        try {
            return new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            authorities
           );
        } catch (AuthenticationException e) {
            return null;
        }
    }

    /**
     * @param authentication
     * @param user
     * @return headers
     * @throws IOException
     * @throws ServletException
     */
    public void createAndRefreshJWT(Authentication authentication, ar.edu.itba.paw.models.User user, HttpServletResponse response) throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalArgumentException();
        }

        claims.put(Constants.JWT_CLAIMS_USERNAME, username);
        Optional<? extends GrantedAuthority> authority = authentication.getAuthorities().stream().findFirst();
        authority.ifPresent(grantedAuthority -> claims.put(Constants.JWT_CLAIMS_ROLE, grantedAuthority.getAuthority()));

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(this.APP_HOST)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_EXPIRATION_MILLIS))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();

        String refreshToken;
        if (user.getRefreshToken() != null) {
            refreshToken = this.refreshTokenService.refresh(user.getRefreshToken());
        } else {
            refreshToken = this.userService.generateRefreshToken(user);
        }

        CookieUtils.setHttpOnlyCookie(response, this.createJWTCookie(token));
        CookieUtils.setHttpOnlyCookie(response, this.createRefreshTokenCookie(refreshToken));
    }

    public void invalidateJWTCookies(HttpServletResponse response) {
        CookieUtils.setHttpOnlyCookie(response, this.createJWTCookie(Constants.EMPTY_COOKIE));
        CookieUtils.setHttpOnlyCookie(response, this.createRefreshTokenCookie(Constants.EMPTY_COOKIE));
    }

    private Cookie createJWTCookie(String token) {
        Cookie cookie = new Cookie(Constants.JWT_COOKIE_NAME, token);
        // No usamos secure porque paw no tiene ssl
        // jwtCookie.setSecure(true);

        int maxAge = token.isEmpty() ? 0 : (int) ((System.currentTimeMillis() + Constants.JWT_EXPIRATION_MILLIS) / 1000);
        cookie.setMaxAge(maxAge); // Seconds
        cookie.setDomain(this.APP_HOST);
        cookie.setPath(this.APP_SUBPATH);

        return cookie;
    }

    private Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie(Constants.JWT_COOKIE_NAME, token);
        // No usamos secure porque paw no tiene ssl
        // jwtCookie.setSecure(true);

        int maxAge = token.isEmpty() ? 0 : (int) ((System.currentTimeMillis() + Constants.JWT_REFRESH_EXPIRATION_MILLIS) / 1000);
        cookie.setMaxAge(maxAge); // Seconds
        cookie.setDomain(this.APP_HOST);
        cookie.setPath(this.APP_SUBPATH + Constants.AUTH_ENDPOINT);

        return cookie;
    }

    private String getSecretKey() throws IOException {
        return "SECRET"; // TODO
//        InputStreamReader streamReader = new InputStreamReader(this.secretResource.getInputStream(), StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(streamReader);
//
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String line; (line = reader.readLine()) != null; ) {
//            stringBuilder.append(line);
//        }
//        return stringBuilder.toString();
    }
}
