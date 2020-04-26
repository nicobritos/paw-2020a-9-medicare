package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String targetUrl = this.getTargetUrl(authentication);
        if (!httpServletResponse.isCommitted()) {
            this.redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetUrl);
        }
    }

    private String getTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isPatient = false;
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(UserRoles.STAFF.name())) {
                return "/staff/home";
            } else if (authority.getAuthority().equals(UserRoles.PATIENT.name())) {
                isPatient = true;
            }
        }
        if (isPatient)
            return "/patient/home";
        return "/login/complete";
    }
}
