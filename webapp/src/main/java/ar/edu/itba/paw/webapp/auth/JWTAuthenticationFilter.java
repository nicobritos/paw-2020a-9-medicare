package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.ExceptionResponseWriter;
import ar.edu.itba.paw.webapp.models.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    private final String secret;

    @Value("classpath:token.key")
    private Resource secretResource;

    public JWTAuthenticationFilter() throws IOException {
        this.secret = this.getSecretKey();
        this.setFilterProcessesUrl("/api/login");
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.equalsIgnoreCase(request.getMethod())) {
            ExceptionResponseWriter.setError(response, Status.METHOD_NOT_ALLOWED);
            return null;
        }

        UserCredentials credentials;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
        } catch (UnrecognizedPropertyException e) {
            ExceptionResponseWriter.setError(response, Status.BAD_REQUEST);
            return null;
        } catch (IOException e) {
            ExceptionResponseWriter.setError(response, Status.SERVICE_UNAVAILABLE);
            return null;
        }

        try {
            return this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new LinkedList<>()
                    ));
        } catch (AuthenticationException e) {
            ExceptionResponseWriter.setError(response, Status.UNAUTHORIZED);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();

        claims.put(Constants.JWT_CLAIMS_USERNAME, ((User) auth.getPrincipal()).getUsername());
        Optional<? extends GrantedAuthority> authority = auth.getAuthorities().stream().findFirst();
        authority.ifPresent(grantedAuthority -> claims.put(Constants.JWT_CLAIMS_ROLE, grantedAuthority.getAuthority()));

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(Constants.ISSUER_INFO)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();

        String refreshToken;
        ar.edu.itba.paw.models.User user = this.userService.findByUsername(((User) auth.getPrincipal()).getUsername()).get();
        if (user.getRefreshToken() != null) {
            refreshToken = this.refreshTokenService.refresh(user.getRefreshToken());
        } else {
            refreshToken = this.userService.generateRefreshToken(user);
        }

        response.addHeader(HttpHeaders.AUTHORIZATION, Constants.TOKEN_BEARER_PREFIX + " " + token);
        response.addHeader(Constants.AUTHORIZATION_REFRESH_HEADER, refreshToken);
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