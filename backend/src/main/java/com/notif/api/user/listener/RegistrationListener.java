package com.notif.api.user.listener;

import com.notif.api.common.events.UserRegisteredEvent;
import com.notif.api.user.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<UserRegisteredEvent> {
    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(UserRegisteredEvent event) {
        String userEmail = event.getUserEmail();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(userEmail, token);

        String subject = "Confirm Registration";
        String confirmationUrl =
                event.getAppUrl() + "/auth/confirm-registration?token=" + token + "&email=" + userEmail;
        String message = "Click the link to verify your account:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}