package com.notif.api.user.infrastructure.event;

import com.notif.api.user.domain.event.UserCreatedEvent;
import com.notif.api.user.application.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    @Value("${app.url}")
    private String appUrl;

    @Value("${api.prefix}")
    private String apiPrefix;

    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;

    @EventListener
    @Async
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        String userEmail = event.getUserEmail();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(userEmail, token);

        String subject = "Confirm Registration";
        String confirmationUrl =
                appUrl + apiPrefix + "/auth/confirm-registration?token=" + token + "&email=" + userEmail;
        String message = "Click the link to verify your account:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);

    }
}