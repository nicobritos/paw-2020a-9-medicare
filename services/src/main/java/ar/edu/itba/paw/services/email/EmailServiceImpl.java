package ar.edu.itba.paw.services.email;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import org.apache.commons.text.StringSubstitutor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.joda.time.DateTimeConstants.*;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MessageSource messageSource;

    private static final String MESSAGE_CANCEL_SOURCE_BODY_PREFIX = "appointment.cancel.email.body";
    private static final String MESSAGE_NEW_APPOINTMENT_SOURCE_BODY_PREFIX = "appointment.new.email.body";
    private static final String MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX = "signup.confirmation.email.body";
    private static final String MESSAGE_SOURCE_DISCLAIMER = "email.disclaimer";

    private void sendEmail(String to, String subject, String html) throws MessagingException{
        InternetAddress[] parsed;
        try {
            parsed = InternetAddress.parse(to);
        } catch (AddressException e) {
            throw new IllegalArgumentException("Not valid email: " + to, e);
        }

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        mailMessage.setSubject(subject, "UTF-8");

        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        helper.setFrom(EmailSender.getEmailUser());
        helper.setTo(parsed);
        helper.setText(html, true);

        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendCanceledAppointmentNotificationEmail(User userCancelling, boolean isDoctorCancelling,
                                                         User userCancelled, Appointment appointment,
                                                         String baseUrl, Locale locale) throws MessagingException {
        String subject = this.messageSource.getMessage("appointment.cancel.email.subject", null, locale);
        String userTitle = isDoctorCancelling ? this.messageSource.getMessage("doctor", null, locale) : this.messageSource.getMessage("patient", null, locale);
        String dowMessage;
        switch (appointment.getFromDate().getDayOfWeek()) {
            case MONDAY:
                dowMessage = "Monday";
                break;
            case TUESDAY:
                dowMessage = "Tuesday";
                break;
            case WEDNESDAY:
                dowMessage = "Wednesday";
                break;
            case THURSDAY:
                dowMessage = "Thursday";
                break;
            case FRIDAY:
                dowMessage = "Friday";
                break;
            case SATURDAY:
                dowMessage = "Saturday";
                break;
            case SUNDAY:
                dowMessage = "Sunday";
                break;
            default:
                dowMessage = null;
        }
        String month;
        switch (appointment.getFromDate().getMonthOfYear()) {
            case JANUARY:
                month = "January";
                break;
            case FEBRUARY:
                month = "February";
                break;
            case MARCH:
                month = "March";
                break;
            case APRIL:
                month = "April";
                break;
            case MAY:
                month = "May";
                break;
            case JUNE:
                month = "June";
                break;
            case JULY:
                month = "July";
                break;
            case AUGUST:
                month = "August";
                break;
            case SEPTEMBER:
                month = "September";
                break;
            case OCTOBER:
                month = "October";
                break;
            case NOVEMBER:
                month = "November";
                break;
            case DECEMBER:
                month = "December";
                break;
            default:
                month = null;
        }

        String html = getCancellingHTML(baseUrl,
                userCancelling, userTitle, appointment,
                this.messageSource.getMessage(dowMessage, null, locale),
                this.messageSource.getMessage(month, null, locale),
                baseUrl + (isDoctorCancelling?"/mediclist/1":""),
                isDoctorCancelling,
                locale);

        sendEmail(userCancelled.getEmail(), subject, html);
    }

    @Override
    public void sendNewAppointmentNotificationEmail(User patient, User doctor, Appointment appointment, Locale locale,
                                             String baseUrl, String motive, String comment) throws MessagingException {
        String subject = this.messageSource.getMessage("appointment.new.email.subject", null, locale);
        String dowMessage;
        switch (appointment.getFromDate().getDayOfWeek()) {
            case MONDAY:
                dowMessage = "Monday";
                break;
            case TUESDAY:
                dowMessage = "Tuesday";
                break;
            case WEDNESDAY:
                dowMessage = "Wednesday";
                break;
            case THURSDAY:
                dowMessage = "Thursday";
                break;
            case FRIDAY:
                dowMessage = "Friday";
                break;
            case SATURDAY:
                dowMessage = "Saturday";
                break;
            case SUNDAY:
                dowMessage = "Sunday";
                break;
            default:
                dowMessage = null;
        }
        String month;
        switch (appointment.getFromDate().getMonthOfYear()) {
            case JANUARY:
                month = "January";
                break;
            case FEBRUARY:
                month = "February";
                break;
            case MARCH:
                month = "March";
                break;
            case APRIL:
                month = "April";
                break;
            case MAY:
                month = "May";
                break;
            case JUNE:
                month = "June";
                break;
            case JULY:
                month = "July";
                break;
            case AUGUST:
                month = "August";
                break;
            case SEPTEMBER:
                month = "September";
                break;
            case OCTOBER:
                month = "October";
                break;
            case NOVEMBER:
                month = "November";
                break;
            case DECEMBER:
                month = "December";
                break;
            default:
                month = null;
        }
        String html = getNewAppointmentHTML(
                baseUrl, doctor, patient, appointment,
                this.messageSource.getMessage(dowMessage, null, locale),
                this.messageSource.getMessage(month, null, locale),
                locale, motive, comment);

        sendEmail(doctor.getEmail(), subject, html);
    }

    @Override
    public void sendEmailConfirmationEmail(User user, String token, String confirmationPageRelativeUrl, Locale locale, String baseUrl) throws MessagingException {

        String subject = this.messageSource.getMessage("signup.confirmation.email.subject", null, locale);
        String confirmationUrl;
        try {
            confirmationUrl = confirmationPageRelativeUrl + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error converting token to UTF-8");
        }
        String html = getConfirmationHTML(baseUrl, confirmationUrl, user, locale);
        sendEmail(user.getEmail(), subject, html);
    }

    private String getCancellingHTML(String baseUrl, User userCancelling, String userTitle, Appointment appointment, String dow, String month, String link, boolean isCancellingDoctor, Locale locale)  {
        Map<String, String> values = new HashMap<>();
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("body", this.messageSource.getMessage(MESSAGE_CANCEL_SOURCE_BODY_PREFIX + ".body",
                new Object[]{
                        userTitle,
                        userCancelling.getDisplayName(),
                        dow,
                        appointment.getFromDate().getDayOfMonth(),
                        month,
                        Integer.toString(appointment.getFromDate().getYear()),
                        ((appointment.getFromDate().getHourOfDay() < 10) ? "0" : "") + appointment.getFromDate().getHourOfDay(),
                        ((appointment.getFromDate().getMinuteOfHour() < 10) ? "0" : "") + appointment.getFromDate().getMinuteOfHour(),
                        ((appointment.getToDate().getHourOfDay() < 10) ? "0" : "") + appointment.getToDate().getHourOfDay(),
                        ((appointment.getToDate().getMinuteOfHour() < 10) ? "0" : "") + appointment.getToDate().getMinuteOfHour()
                },
                locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_CANCEL_SOURCE_BODY_PREFIX + ".title", null, locale));
        values.put("link", link);
        if(isCancellingDoctor) {
            values.put("buttonText", this.messageSource.getMessage(MESSAGE_CANCEL_SOURCE_BODY_PREFIX + ".buttonText.toPatient", null, locale));
        } else {
            values.put("buttonText", this.messageSource.getMessage(MESSAGE_CANCEL_SOURCE_BODY_PREFIX + ".buttonText.toDoctor", null, locale));
        }
        EmailFormatter emailFormatter = new EmailFormatter();
        String html;
        try {
            html = emailFormatter.format(emailFormatter.getHTMLFromFilename("cancel"));
        } catch (IOException e){
            throw new RuntimeException("Can't find HTML file cancel.html");
        }
        StringSubstitutor substitutor = new StringSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }

    private String getNewAppointmentHTML(String baseUrl, User doctor, User patient, Appointment appointment, String dow, String month, Locale locale, String motive, String comment) {
        Map<String, String> values = new HashMap<>();
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("body", this.messageSource.getMessage(MESSAGE_NEW_APPOINTMENT_SOURCE_BODY_PREFIX + ".body",
                new Object[]{
                        doctor.getFirstName(),
                        patient.getDisplayName(),
                        dow,
                        appointment.getFromDate().getDayOfMonth(),
                        month,
                        Integer.toString(appointment.getFromDate().getYear()),
                        ((appointment.getFromDate().getHourOfDay() < 10) ? "0" : "") + appointment.getFromDate().getHourOfDay(),
                        ((appointment.getFromDate().getMinuteOfHour() < 10) ? "0" : "") + appointment.getFromDate().getMinuteOfHour(),
                        ((appointment.getToDate().getHourOfDay() < 10) ? "0" : "") + appointment.getToDate().getHourOfDay(),
                        ((appointment.getToDate().getMinuteOfHour() < 10) ? "0" : "") + appointment.getToDate().getMinuteOfHour(),
                        motive
                },
                locale));
        values.put("comment", this.messageSource.getMessage(MESSAGE_NEW_APPOINTMENT_SOURCE_BODY_PREFIX + ".comment", new Object[]{comment}, locale));
        values.put("link", baseUrl);
        values.put("buttonText", this.messageSource.getMessage(MESSAGE_NEW_APPOINTMENT_SOURCE_BODY_PREFIX + ".buttonText", new Object[]{comment}, locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_NEW_APPOINTMENT_SOURCE_BODY_PREFIX + ".title", null, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html;
        if (comment != null && !comment.isEmpty()) {
            try {
                html = emailFormatter.format(emailFormatter.getHTMLFromFilename("newAppointmentWithComment"));
            } catch (IOException e){
                throw new RuntimeException("Can't find HTML file newAppointmentWithComment.html");
            }
        } else {
            try {
                html = emailFormatter.format(emailFormatter.getHTMLFromFilename("newAppointment"));
            } catch (IOException e){
                throw new RuntimeException("Can't find HTML file newAppointment.html");
            }
        }
        StringSubstitutor substitutor = new StringSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }

    private String getConfirmationHTML(String baseUrl, String url, User user, Locale locale) {
        Map<String, String> values = new HashMap<>();
        values.put("url", url);
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("button", this.messageSource.getMessage(MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX + ".button", null, locale));
        values.put("button_url", this.messageSource.getMessage(MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX + ".button_url", null, locale));
        values.put("body", this.messageSource.getMessage(MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX + ".body", null, locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX + ".title", new Object[]{user.getFirstName()}, locale));
        values.put("subtitle", this.messageSource.getMessage(MESSAGE_CONFIRMATION_SOURCE_BODY_PREFIX + ".subtitle", new Object[]{user.getFirstName()}, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html;
        try {
            html = emailFormatter.format(emailFormatter.getHTMLFromFilename("signup"));
        } catch (IOException e){
            throw new RuntimeException("Can't find HTML file signup.html");
        }
        StringSubstitutor substitutor = new StringSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }

}
