package ar.edu.itba.paw.webapp.events;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.email.EmailFormatter;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class UserConfirmationTokenGenerationEventListener implements ApplicationListener<UserConfirmationTokenGenerationEvent> {
    private static final String MESSAGE_SOURCE_BODY_PREFIX = "signup.confirmation.email.body";
    private static final String MESSAGE_SOURCE_DISCLAIMER = "email.disclaimer";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConfirmationTokenGenerationEventListener.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ServletContext context;
    private String logoPath = "img/whiteLogo.svg";
    private String logoImageType = "image/svg+xml";

    @Override
    @Async
    public void onApplicationEvent(UserConfirmationTokenGenerationEvent userConfirmationTokenGenerationEvent) {
        String token = this.userService.generateVerificationToken(userConfirmationTokenGenerationEvent.getUser());

        String subject = this.messageSource.getMessage("signup.confirmation.email.subject", null, userConfirmationTokenGenerationEvent.getLocale());
        try {
            String confirmationUrl = userConfirmationTokenGenerationEvent.getUrl() + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.name());

            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(new InternetAddress("confirmation@medicare.com", "MediCare"));
            mimeMessageHelper.setTo(userConfirmationTokenGenerationEvent.getUser().getEmail());
            mimeMessageHelper.setText(this.getHTML(userConfirmationTokenGenerationEvent.getBaseUrl(), confirmationUrl, userConfirmationTokenGenerationEvent.getUser(), userConfirmationTokenGenerationEvent.getLocale()), true);
            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("Error sending email: ", e);
        }
    }

    private String getHTML(String baseUrl, String url, User user, Locale locale) throws IOException {
        Map<String, String> values = new HashMap<>();
        values.put("url", url);
        values.put("baseUrl", baseUrl);
        values.put("year", String.valueOf(DateTime.now().getYear()));
        values.put("button", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".button", null, locale));
        values.put("button_url", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".button_url", null, locale));
        values.put("body", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".body", null, locale));
        values.put("disclaimer", this.messageSource.getMessage(MESSAGE_SOURCE_DISCLAIMER, null, locale));
        values.put("title", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".title", new Object[] {user.getFirstName()}, locale));
        values.put("subtitle", this.messageSource.getMessage(MESSAGE_SOURCE_BODY_PREFIX + ".subtitle", new Object[] {user.getFirstName()}, locale));

        EmailFormatter emailFormatter = new EmailFormatter();
        String html = emailFormatter.format(emailFormatter.getHTMLFromFilename("signup"));
        StrSubstitutor substitutor = new StrSubstitutor(values, "${", "}");
        return substitutor.replace(html);
    }
}
