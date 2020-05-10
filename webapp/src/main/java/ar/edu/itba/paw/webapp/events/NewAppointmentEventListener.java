package ar.edu.itba.paw.webapp.events;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.email.EmailFormatter;
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
public class NewAppointmentEventListener implements ApplicationListener<NewAppointmentEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewAppointmentEventListener.class);
    private static final String MESSAGE_SOURCE_BODY_PREFIX = "appointment.new.email.body";
    private static final String MESSAGE_SOURCE_DISCLAIMER = "email.disclaimer";

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void onApplicationEvent(NewAppointmentEvent newAppointmentEvent) {
        String subject = this.messageSource.getMessage("appointment.new.email.subject", null, newAppointmentEvent.getLocale());
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            String dowMessage;
            switch (newAppointmentEvent.getAppointment().getFromDate().getDayOfWeek()){
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
            switch (newAppointmentEvent.getAppointment().getFromDate().getMonthOfYear()){
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
                            newAppointmentEvent.getBaseUrl(),
                            newAppointmentEvent.getDoctor(),
                            newAppointmentEvent.getPatient(),
                            newAppointmentEvent.getAppointment(),
                            this.messageSource.getMessage(dowMessage, null, newAppointmentEvent.getLocale()),
                            this.messageSource.getMessage(month, null, newAppointmentEvent.getLocale()),
                            newAppointmentEvent.getLocale()),
                    true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress("notifications@medicare.com", "MediCare"));
            mimeMessageHelper.setTo(newAppointmentEvent.getDoctor().getEmail());
            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("Error sending new appointment email: ", e);
        }
    }

    private String getHTML(String baseUrl, User doctor, User patient, Appointment appointment, String dow, String month, Locale locale) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("body", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".body",
                new Object[]{
                        doctor.getFirstName(),
                        patient.getDisplayName(),
                        dow,
                        appointment.getFromDate().getDayOfMonth(),
                        month,
                        Integer.toString(appointment.getFromDate().getYear()),
                        ((appointment.getFromDate().getHourOfDay() < 10)?"0":"") + appointment.getFromDate().getHourOfDay(),
                        ((appointment.getFromDate().getMinuteOfHour() < 10)?"0":"") + appointment.getFromDate().getMinuteOfHour(),
                        ((appointment.getToDate().getHourOfDay() < 10)?"0":"") + appointment.getToDate().getHourOfDay(),
                        ((appointment.getToDate().getMinuteOfHour() < 10)?"0":"") + appointment.getToDate().getMinuteOfHour()
                },
                locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".title", null, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html = emailFormatter.format(emailFormatter.getHTMLFromFilename("newAppointment"));
        StrSubstitutor substitutor = new StrSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }
}
