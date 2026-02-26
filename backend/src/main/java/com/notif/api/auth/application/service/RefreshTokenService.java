package com.notif.api.auth.application.service;

import com.notif.api.auth.domain.model.RefreshToken;

import java.util.UUID;

/**
 * Service interface for managing refresh token lifecycle.
 *
 * Handles creation, validation, revocation, and cleanup of refresh tokens
 * used for authentication session management.
 */
public interface RefreshTokenService {
    RefreshToken generateRefreshToken(UUID userId);
    RefreshToken validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    void revokeAllUserTokens(UUID userId);
    void deleteAllUserTokens(UUID userId);
}