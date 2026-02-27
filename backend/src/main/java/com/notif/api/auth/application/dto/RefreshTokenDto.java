package com.notif.api.auth.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Data transfer object used for refresh token operations.
 */
@Data
@Builder
public class RefreshTokenDto {
    private String token;
    private UUID userId;
}