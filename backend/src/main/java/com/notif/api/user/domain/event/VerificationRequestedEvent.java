package com.notif.api.user.domain.event;

import com.notif.api.core.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Event published when a verification request is triggered.
 */
@Getter
@AllArgsConstructor
public class VerificationRequestedEvent implements DomainEvent {
    private final String userEmail;
    private final String token;
    private final LocalDateTime occurredOn = LocalDateTime.now();

    @Override
    public String getEventType() {
        return "VERIFICATION_REQUEST";
    }
}