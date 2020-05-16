package ar.edu.itba.paw.webapp.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class CustomSecurityRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSecurityRepository.class);
    private static final Map<User, Collection<SecurityContext>> userContext = new HashMap<>();

    static void onLogin(SecurityContext context) {
        if (context != null && context.getAuthentication() != null && context.getAuthentication().getPrincipal() instanceof User)
            userContext.computeIfAbsent((User) context.getAuthentication().getPrincipal(), user -> new LinkedList<>()).add(context);
    }

    static void onLogout(SecurityContext context) {
        if (context != null && context.getAuthentication() != null)
            userContext.remove((User) context.getAuthentication().getPrincipal());
    }

    public static synchronized void propagateAuthorities(User user, Collection<GrantedAuthority> authorities) {
        Collection<SecurityContext> originalContexts = userContext.get(user);
        if (originalContexts == null) {
            LOGGER.error("Error propagating authorities: stored SecurityContexts is null. Are they being saved? For user: {}", user.getUsername());
            return;
        }
        if (originalContexts.isEmpty()) {
            LOGGER.error("Error propagating authorities: stored SecurityContexts is empty. Are they being saved? For user: {}", user.getUsername());
            return;
        }

        Collection<SecurityContext> contexts = new LinkedList<>(originalContexts); // Prevent modification of collection when iterating
        for (SecurityContext context : contexts) {
            Authentication authentication = context.getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                originalContexts.remove(context);
                if (originalContexts.isEmpty())
                    userContext.remove(user);
                continue;
            }

            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), new LinkedList<>(authorities));
            context.setAuthentication(newAuthentication);
        }
    }
}
