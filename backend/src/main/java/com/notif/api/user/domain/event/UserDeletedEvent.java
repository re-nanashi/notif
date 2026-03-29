package com.notif.api.user.domain.event;

import com.notif.api.core.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published when a user is deleted.
 */
@Getter
@AllArgsConstructor
public class UserDeletedEvent implements DomainEvent {
    private final UUID userId;
    private final Instant occurredOn = Instant.now();

    @Override
    public String getEventType() {
        return "USER_DELETED";
    }
}