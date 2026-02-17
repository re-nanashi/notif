package com.notif.api.core.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime getOccurredOn();
    String getEventType();
}