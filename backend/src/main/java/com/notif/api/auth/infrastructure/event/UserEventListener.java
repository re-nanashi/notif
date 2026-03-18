package com.notif.api.auth.infrastructure.event;

import com.notif.api.auth.application.service.SessionRevocationService;
import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.user.domain.event.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

/**
 * Listens to user domain events to perform authentication cleanup tasks.
 */
@Component
@RequiredArgsConstructor
public class UserEventListener {
    private SessionRevocationService sessionRevocationService;

    /**
     * Handles user deletion events asynchronously to avoid blocking
     * the main application thread.
     */
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        UUID userId = event.getUserId();

        // Soft delete; revoke all user sessions and tokens tied the sessions
        sessionRevocationService.revokeAllUserSessions(userId, SessionRevokedReason.USER_DELETED);

        // TODO: Add audit logging
    }
}