package com.notif.api.user.infrastructure.event;

import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.service.UserService;
import com.notif.api.user.domain.event.VerificationRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listens VerificationRequestedEvent.
 * Constructs a confirmation URL then sends a verification email to the new user's email.
 */
@Component
@RequiredArgsConstructor
public class VerificationTokenEventListener {
    @Value("${app.url}")
    private String appUrl;

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserService userService;
    private final JavaMailSender mailSender;

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleVerificationRequestedEvent(VerificationRequestedEvent event) {
        UserResponse user = userService.getUserById(event.getUserId());
        String token = event.getToken();

        // Extract user information
        String userName = user.getFirstName();
        String userEmail = user.getEmail();

        // Construct the full confirmation URL with token and email
        String confirmationUrl =
                appUrl + apiPrefix + "/auth/confirm-registration?token=" + token + "&email=" + userEmail;

        // Compose email then send
        String subject = "Welcome aboard, " + userName + "!";
        String message = "Click the link below to verify your account:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}