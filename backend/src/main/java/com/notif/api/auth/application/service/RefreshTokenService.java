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
    RefreshTokenDto generateRefreshToken(UUID userId);
    RefreshTokenDto validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    void revokeAllUserTokens(UUID userId);
    void deleteAllUserTokens(UUID userId);
}