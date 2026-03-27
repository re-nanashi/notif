package com.notif.api.user.domain.event;

import com.notif.api.core.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a User's password is changed.
 * Contains basic information about the user.
 */
@Getter
@AllArgsConstructor
public class PasswordChangedEvent implements DomainEvent {
    private final UUID userId;
    private final LocalDateTime occurredOn = LocalDateTime.now();

    @Override
    public String getEventType() {
        return "USER_PASSWORD_CHANGED";
    }
}