package com.notif.api.auth.infrastructure.event;

import com.notif.api.auth.application.service.RefreshTokenService;
import com.notif.api.user.domain.event.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

/**
 * Listens to user domain events to perform authentication cleanup tasks.
 * Handles user deletion events by removing associated refresh tokens.
 */
@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final RefreshTokenService tokenService;

    /**
     * Handles user deletion events asynchronously to avoid blocking
     * the main application thread.
     */
    @EventListener
    @TransactionalEventListener
    @Async
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        UUID userId = event.getUserId();

        // Cleanup authentication tokens for deleted user
        tokenService.deleteAllUserTokens(userId);

        // TODO: Add audit logging
    }
}