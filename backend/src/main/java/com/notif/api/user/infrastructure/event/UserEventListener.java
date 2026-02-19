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
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

/**
 * Listens for UserCreatedEvent and sends a verification email to the new user.
 * Generates a unique verification token and constructs the confirmation URL.
 */
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        // Generate a unique verification token for the new user
        String userEmail = event.getUserEmail();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(userEmail, token);

        // Construct the full confirmation URL with token and email
        String confirmationUrl =
                appUrl + apiPrefix + "/auth/confirm-registration?token=" + token + "&email=" + userEmail;

        // Compose email then send
        String subject = "Confirm Registration";
        String message = "Click the link to verify your account:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}