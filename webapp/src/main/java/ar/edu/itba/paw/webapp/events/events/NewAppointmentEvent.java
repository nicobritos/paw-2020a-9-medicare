package ar.edu.itba.paw.webapp.events.events;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class NewAppointmentEvent extends ApplicationEvent {
    private User patient;
    private User doctor;
    private Appointment appointment;
    private Locale locale;
    private String baseUrl;
    private String motive;
    private String comment;

    public NewAppointmentEvent(User patient, User doctor, Appointment appointment, Locale locale, String baseUrl, String motive, String comment) {
        super(patient);
        this.baseUrl = baseUrl;
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.locale = locale;
        this.motive = motive;
        this.comment = comment;
    }

    public User getPatient() {
        return patient;
    }

    public User getDoctor() {
        return doctor;
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

    public String getMotive() {
        return motive;
    }

    public String getComment() {
        return comment;
    }
}
