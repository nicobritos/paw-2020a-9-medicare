package ar.edu.itba.paw.webapp.events;

import ar.edu.itba.paw.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class SignUpEvent extends ApplicationEvent {
    private User user;
    private String url;
    private Locale locale;

    public SignUpEvent(User user, String url, Locale locale) {
        super(user);
        this.user = user;
        this.url = url;
        this.locale = locale;
    }

    public User getUser() {
        return this.user;
    }

    public String getUrl() {
        return this.url;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
