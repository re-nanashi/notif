package com.notif.api.auth.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object used for device operations.
 */
@Data
@Builder
public class DeviceDto {
    private UUID id;
    private UUID deviceId;
    private String name;
    private String model;
    private Instant lastSeenAt;
}