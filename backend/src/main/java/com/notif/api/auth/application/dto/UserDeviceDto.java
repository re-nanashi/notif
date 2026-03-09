package com.notif.api.auth.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object used for user device operations.
 */
@Data
@Builder
public class UserDeviceDto {
    private UUID id;
    private UUID userId;
    private UUID deviceId;
    private String nickname;
    private String userAgent;
    private Instant lastSeenAt;
}