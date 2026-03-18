package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.SessionDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing sessions.
 */
public interface SessionService {
    SessionDto createSession(UUID userId, UUID deviceId, String ipAddress);
    SessionDto getActiveSession(UUID sessionId);
    Optional<SessionDto> getActiveSessionOnDevice(UUID deviceId);
}