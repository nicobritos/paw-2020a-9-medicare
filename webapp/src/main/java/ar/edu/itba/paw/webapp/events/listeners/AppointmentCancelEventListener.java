package ar.edu.itba.paw.webapp.events.listeners;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.email.EmailFormatter;
import ar.edu.itba.paw.webapp.events.events.AppointmentCancelEvent;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.joda.time.DateTimeConstants.*;

@Component
public class AppointmentCancelEventListener implements ApplicationListener<AppointmentCancelEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentCancelEventListener.class);
    private static final String MESSAGE_SOURCE_BODY_PREFIX = "appointment.cancel.email.body";
    private static final String MESSAGE_SOURCE_DISCLAIMER = "email.disclaimer";

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void onApplicationEvent(AppointmentCancelEvent appointmentCancelEvent) {
        String subject = this.messageSource.getMessage("appointment.cancel.email.subject", null, appointmentCancelEvent.getLocale());
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            String userTitle = appointmentCancelEvent.isCancellingStaff() ? this.messageSource.getMessage("doctor", null, appointmentCancelEvent.getLocale()) : this.messageSource.getMessage("patient", null, appointmentCancelEvent.getLocale());
            String dowMessage;
            switch (appointmentCancelEvent.getAppointment().getFromDate().getDayOfWeek()) {
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
            switch (appointmentCancelEvent.getAppointment().getFromDate().getMonthOfYear()) {
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
            mimeMessageHelper.setText(
                    this.getHTML(
                            appointmentCancelEvent.getBaseUrl(),
                            appointmentCancelEvent.getUserCancelling(),
                            userTitle,
                            appointmentCancelEvent.getAppointment(),
                            this.messageSource.getMessage(dowMessage, null, appointmentCancelEvent.getLocale()),
                            this.messageSource.getMessage(month, null, appointmentCancelEvent.getLocale()),
                            appointmentCancelEvent.getLocale()),
                    true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress("notifications@medicare.com", "MediCare"));
            mimeMessageHelper.setTo(appointmentCancelEvent.getUserCancelled().getEmail());
            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("Error sending appointment cancellation email: ", e);
        }
    }

    private String getHTML(String baseUrl, User userCancelling, String userTitle, Appointment appointment, String dow, String month, Locale locale) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("body", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".body",
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
        values.put("title", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".title", null, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html = emailFormatter.format(emailFormatter.getHTMLFromFilename("cancel"));
        StrSubstitutor substitutor = new StrSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }
}
