package com.notif.api.auth.application.service;

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
import java.util.List;
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
    public RefreshToken generateRefreshToken(UUID userId) {
        String tokenString = UUID.randomUUID().toString();

        RefreshToken token = RefreshToken.builder()
                .token(tokenString)
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration))
                .revoked(false)
                .build();

        return tokenRepository.save(token);
    }

    /**
     * Validates refresh token status and expiration.
     */
    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken existingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired refresh token. Please log in again."));

        // Check refresh token validity
        if (existingToken.isRevoked()) {
            throw new TokenRevokedException("Invalid refresh token. Please log in again.");
        }
        if (existingToken.isTokenExpired()) {
            throw new TokenExpiredException("Expired refresh token. Please log in again.");
        }

        return existingToken;
    }

    /**
     * Revokes a specific refresh token.
     */
    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken existingToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired refresh token. Please log in again."));

        existingToken.setRevoked(true);

        tokenRepository.save(existingToken);
    }

    /**
     * Revokes all refresh tokens associated with a user.
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(UUID userId) {
        List<RefreshToken> userTokens = tokenRepository.findAllByUserId(userId);

        userTokens.forEach(token -> {
            token.setRevoked(true);
            tokenRepository.save(token);
        });
    }


    /**
     * Deletes all refresh tokens belonging to a user.
     */
    @Override
    @Transactional
    public void deleteAllUserTokens(UUID userId) {
        List<RefreshToken> userTokens = tokenRepository.findAllByUserId(userId);

        userTokens.forEach(token -> {
            tokenRepository.deleteById(token.getId());
        });
    }
}