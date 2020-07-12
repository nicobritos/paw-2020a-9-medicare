package ar.edu.itba.paw.webapp.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final String secret;
    @Value("classpath:token.key")
    private Resource secretResource;

    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager) throws IOException {
        super(authManager);
        this.secret = this.getSecretKey();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(Constants.TOKEN_BEARER_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = this.getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null)
            return null;

        // Procesamos los claims guardados. En este caso, el username y el rol
        Claims claims = Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token.replace(Constants.TOKEN_BEARER_PREFIX, ""))
                .getBody();

        if (claims.getExpiration().before(new Date()))
            return null;

        Object o = claims.get(Constants.JWT_CLAIMS_USERNAME);
        String user = o != null ? o.toString() : null;

        Collection<? extends GrantedAuthority> authorities;
        o = claims.get(Constants.JWT_CLAIMS_ROLE);
        String role = o != null ? o.toString() : null;
        if (role != null)
            authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        else
            authorities = Collections.emptyList();

        return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
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
