package com.notif.api.core.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccurredOn();
    String getEventType();
}