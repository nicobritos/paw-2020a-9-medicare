package ar.edu.itba.paw.webapp.events;

import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class SignUpListener implements ApplicationListener<SignUpEvent> {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private String applicationName;

    @Override
    public void onApplicationEvent(SignUpEvent signUpEvent) {
        String token = this.userService.generateVerificationToken(signUpEvent.getUser());

        String subject = this.messageSource.getMessage("signup.confirmation.email.subject", null, signUpEvent.getLocale()); // todo
        String confirmationUrl = "";
        try {
            confirmationUrl = signUpEvent.getUrl() + "?token=" + URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // todo
        }
        String message = this.messageSource.getMessage("signup.confirmation.email.body", new Object[] {confirmationUrl}, signUpEvent.getLocale());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(signUpEvent.getUser().getEmail());
        mailMessage.setFrom(this.applicationName);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        this.mailSender.send(mailMessage);
    }
}
