package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.RefreshTokenDto;
import com.notif.api.auth.domain.exception.TokenExpiredException;
import com.notif.api.auth.domain.exception.TokenNotFoundException;
import com.notif.api.auth.domain.exception.TokenRevokedException;
import com.notif.api.auth.domain.model.RefreshToken;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of refresh token lifecycle management.
 * Responsible for refresh token generation, token validation, token revocation, and clean up operations.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository tokenRepository;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    /**
     * Generates a new refresh token with fresh expiration timestamp.
     * Uses REQUIRES_NEW propagation to ensure token persistence is independent of any existing transaction context.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RefreshTokenDto generateRefreshToken(UUID userId) {
        String tokenString = UUID.randomUUID().toString();

        RefreshToken token = RefreshToken.builder()
                .token(tokenString)
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration))
                .revoked(false)
                .build();

        RefreshToken savedToken = tokenRepository.save(token);

        return convertTokenToDto(savedToken);
    }

    /**
     * Validates refresh token status and expiration.
     */
    @Override
    @Transactional(readOnly = true)
    public RefreshTokenDto validateRefreshToken(String tokenString) {
        RefreshToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired refresh token. Please log in again."));

        // Check refresh token validity
        if (token.isRevoked()) {
            throw new TokenRevokedException("Invalid refresh token. Please log in again.");
        }
        if (token.isTokenExpired()) {
            throw new TokenExpiredException("Expired refresh token. Please log in again.");
        }

        return convertTokenToDto(token);
    }

    /**
     * Revokes a specific refresh token.
     */
    @Override
    @Transactional
    public void revokeRefreshToken(String tokenString) {
        RefreshToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired refresh token. Please log in again."));

        token.setRevoked(true);

        tokenRepository.save(token);
    }

    /**
     * Revokes all refresh tokens associated with a user.
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(UUID userId) {
        tokenRepository.revokeAllByUserId(userId);
    }

    /**
     * Deletes all refresh tokens belonging to a user.
     */
    @Override
    @Transactional
    public void deleteAllUserTokens(UUID userId) {
        tokenRepository.deleteAllByUserId(userId);
    }

    /**
     * Maps domain RefreshToken entity to DTO.
     */
    private RefreshTokenDto convertTokenToDto(RefreshToken token) {
        return RefreshTokenDto.builder()
                .token(token.getToken())
                .userId(token.getUserId())
                .build();
    }
}