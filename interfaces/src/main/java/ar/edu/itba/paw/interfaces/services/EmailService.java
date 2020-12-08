package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.util.Locale;

public interface EmailService {

    void sendCanceledAppointmentNotificationEmail(User userCancelling, boolean isDoctorCancelling,
                                                  User userCancelled, Appointment appointment,
                                                  String baseUrl, Locale locale) throws MessagingException;

    void sendNewAppointmentNotificationEmail(Appointment appointment, Locale locale,
                                             String baseUrl) throws MessagingException;

    void sendEmailConfirmationEmail(User user, String token, String confirmationPageRelativeUrl, Locale locale, String baseUrl) throws MessagingException;

    void scheduleNotifyAppointmentEmail(Appointment appointment, Locale locale,
                                        String baseUrl);
    @PostConstruct
    void initScheduleEmails();

}
