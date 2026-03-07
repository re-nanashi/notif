package com.notif.api.auth.application.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO that holds basic information about the client for an authentication request.
 */
@Data
@Builder
public class AuthenticationRequestContext {
    private String userAgent;
    private String clientIp;
    private String deviceId;
    private String sessionId;
}