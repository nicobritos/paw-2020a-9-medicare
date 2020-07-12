package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.*;

@Component
public class JWTAuthenticator {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    private final String secret;

    public JWTAuthenticator() throws IOException {
        this.secret = this.getSecretKey();
    }

    public Authentication attemptAuthentication(UserCredentials credentials, HttpServletResponse response) throws AuthenticationException {
        try {
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new LinkedList<>()
                    ));
        } catch (AuthenticationException e) {
            return null;
        }
    }

    public Authentication attemptAuthentication(UserCredentials credentials, HttpServletResponse response, Collection<? extends GrantedAuthority> authorities) throws AuthenticationException {
        try {
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            authorities
                    ));
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
    public Map<String, String> createAndRefreshJWT(Authentication authentication, ar.edu.itba.paw.models.User user) throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();

        claims.put(Constants.JWT_CLAIMS_USERNAME, ((User) authentication.getPrincipal()).getUsername());
        Optional<? extends GrantedAuthority> authority = authentication.getAuthorities().stream().findFirst();
        authority.ifPresent(grantedAuthority -> claims.put(Constants.JWT_CLAIMS_ROLE, grantedAuthority.getAuthority()));

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(Constants.ISSUER_INFO)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();

        String refreshToken;
        if (user.getRefreshToken() != null) {
            refreshToken = this.refreshTokenService.refresh(user.getRefreshToken());
        } else {
            refreshToken = this.userService.generateRefreshToken(user);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Constants.TOKEN_BEARER_PREFIX + " " + token);
        headers.put(Constants.AUTHORIZATION_REFRESH_HEADER, refreshToken);
        return headers;
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
