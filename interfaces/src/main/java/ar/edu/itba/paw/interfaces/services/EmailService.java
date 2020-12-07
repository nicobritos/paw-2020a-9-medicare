package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;

import javax.mail.MessagingException;
import java.util.Locale;

public interface EmailService {

    void sendCanceledAppointmentNotificationEmail(User userCancelling, boolean isDoctorCancelling,
                                                  User userCancelled, Appointment appointment,
                                                  String baseUrl, Locale locale) throws MessagingException;

    void sendNewAppointmentNotificationEmail(User patient, User doctor, Appointment appointment, Locale locale,
                                             String baseUrl, String motive, String comment) throws MessagingException;

    void sendEmailConfirmationEmail(User user, String token, String confirmationPageRelativeUrl, Locale locale, String baseUrl) throws MessagingException;

}
