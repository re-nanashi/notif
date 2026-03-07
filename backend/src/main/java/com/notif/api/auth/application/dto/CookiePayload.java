package com.notif.api.auth.application.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the payload data stored in cookies for authentication, including the refresh token
 * and the associated device ID.
 */
@Data
@Builder
public class CookiePayload {
    private String refreshToken;
    private String deviceId;
}