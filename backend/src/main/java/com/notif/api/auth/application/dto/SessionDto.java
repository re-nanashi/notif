package com.notif.api.auth.application.dto;

import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.domain.model.SessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object used for session operations.
 */
@Data
@Builder
public class SessionDto {
    private UUID id;
    private UUID userId;
    private UUID deviceId;
    private String ipAddress;
    private SessionStatus status;
    private Instant revokedAt;
    private SessionRevokedReason revokedReason;
    private Instant lastActivityAt;
    private Instant expiresAt;
}