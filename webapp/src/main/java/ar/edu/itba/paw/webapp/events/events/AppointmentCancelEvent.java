package ar.edu.itba.paw.webapp.events.events;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class AppointmentCancelEvent extends ApplicationEvent {
    private User userCancelling;
    private boolean isCancellingDoctor;
    private User userCancelled;
    private Appointment appointment;
    private Locale locale;
    private String baseUrl;

    public AppointmentCancelEvent(User userCancelling, boolean isCancellingDoctor, User userCancelled, Appointment appointment, Locale locale, String baseUrl) {
        super(userCancelling);
        this.baseUrl = baseUrl;
        this.userCancelling = userCancelling;
        this.isCancellingDoctor = isCancellingDoctor;
        this.userCancelled = userCancelled;
        this.appointment = appointment;
        this.locale = locale;
    }

    public User getUserCancelling() {
        return userCancelling;
    }

    public User getUserCancelled() {
        return userCancelled;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isCancellingDoctor() {
        return isCancellingDoctor;
    }
}