package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.RefreshTokenDto;

import java.util.UUID;

/**
 * Service interface for managing refresh token lifecycle.
 *
 * Handles creation, validation, revocation, and cleanup of refresh tokens
 * used for authentication session management.
 */
public interface RefreshTokenService {
    RefreshTokenDto generateToken(UUID sessionId);
    RefreshTokenDto rotate(String tokenString);
    RefreshTokenDto getToken(String tokenString);
    void validateToken(RefreshTokenDto tokenDto);
}