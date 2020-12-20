package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.models.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
        UsernamePasswordAuthenticationToken authentication = this.getAuthentication(req);

        if (authentication != null)
            SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = this.parseAuthentication(request);
        request.setAttribute(Constants.VALID_JWT_REQUEST_ATTRIBUTE, token != null);
        return token;
    }

    protected UsernamePasswordAuthenticationToken parseAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        Cookie jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(Constants.JWT_COOKIE_NAME)).findFirst().orElse(null);
        if (jwtCookie == null)
            return null;

        // Procesamos los claims guardados. En este caso, el username y el rol
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(jwtCookie.getValue())
                    .getBody();
        } catch (Exception e) {
            return null;
        }

        if (claims.getExpiration().before(new Date()))
            return null;

        Object o = claims.get(Constants.JWT_CLAIMS_USERNAME);
        Integer id = o != null ? (int) o : null;

        Collection<? extends GrantedAuthority> authorities;
        o = claims.get(Constants.JWT_CLAIMS_ROLE);
        String role = o != null ? o.toString() : null;
        if (role != null)
            authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        else
            authorities = Collections.emptyList();

        return id != null ? new UsernamePasswordAuthenticationToken(new User(id.toString(), "", authorities), "", authorities) : null;
    }

    private String getSecretKey() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("token.key");
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
