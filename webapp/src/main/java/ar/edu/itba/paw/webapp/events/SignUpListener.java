package ar.edu.itba.paw.webapp.events;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.email.EmailFormatter;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class SignUpListener implements ApplicationListener<SignUpEvent> {
    private static final String MESSAGE_SOURCE_BODY_PREFIX = "signup.confirmation.email.body";
    private static final String MESSAGE_SOURCE_DISCLAIMER = "email.disclaimer";

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(SignUpEvent signUpEvent) {
        String token = this.userService.generateVerificationToken(signUpEvent.getUser());

        String subject = this.messageSource.getMessage("signup.confirmation.email.subject", null, signUpEvent.getLocale());
        try {
            String confirmationUrl = signUpEvent.getUrl() + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.name());

            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(this.getHTML(signUpEvent.getBaseUrl(), confirmationUrl, signUpEvent.getUser(), signUpEvent.getLocale()), true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress("confirmation@medicare.com", "MediCare"));
            mimeMessageHelper.setTo(signUpEvent.getUser().getEmail());

            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace(); // TODO
        }
    }

    private String getHTML(String baseUrl, String url, User user, Locale locale) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("url", url);
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("button", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + "." + "button", null, locale));
        values.put("button_url", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + "." + "button_url", null, locale));
        values.put("body", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + "." + "body", null, locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + "." + "title", new Object[] {user.getFirstName()}, locale));
        values.put("subtitle", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + "." + "subtitle", new Object[] {user.getFirstName()}, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html = emailFormatter.format(emailFormatter.getHTMLFromFilename("signup"));
        StrSubstitutor substitutor = new StrSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }
}
