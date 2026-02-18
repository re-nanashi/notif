package com.notif.api.user.domain.event;

import com.notif.api.core.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a new User is created.
 * Contains basic information about the user.
 */
@Getter
@AllArgsConstructor
public class UserCreatedEvent implements DomainEvent {
    private final UUID userId;
    private final String userEmail;
    private final LocalDateTime occurredOn = LocalDateTime.now();

    @Override
    public String getEventType() {
        return "USER_CREATED";
    }
}