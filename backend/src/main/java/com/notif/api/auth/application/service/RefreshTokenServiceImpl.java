package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.RefreshTokenDto;
import com.notif.api.auth.domain.exception.TokenExpiredException;
import com.notif.api.auth.domain.exception.TokenNotFoundException;
import com.notif.api.auth.domain.exception.TokenRevokedException;
import com.notif.api.auth.domain.model.RefreshToken;
import com.notif.api.auth.domain.model.Session;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import com.notif.api.core.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of refresh token lifecycle management.
 * Responsible for refresh token generation, token validation, token revocation, and clean up operations.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository tokenRepository;
    private final EntityManager entityManager;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration; // 7 days in seconds

    /**
     * Generates a new refresh token with fresh expiration timestamp.
     * Uses REQUIRES_NEW propagation to ensure token persistence is independent of any existing transaction context.
     */
    @Override
    @Transactional
    public RefreshTokenDto generateToken(UUID sessionId) {
        String tokenString = UUID.randomUUID().toString();
        String tokenHash = DigestUtils.sha256Hex(tokenString);
        Session sessionProxy = entityManager.getReference(Session.class, sessionId);

        RefreshToken token = RefreshToken.builder()
                .token(tokenHash)
                .session(sessionProxy)
                .expiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
                .build();

        RefreshToken savedToken = tokenRepository.save(token);

        return convertTokenToDto(tokenString, savedToken);
    }

    /**
     * Performs refresh token rotation by consuming the current token and issuing a new one.
     */
    @Override
    @Transactional
    public RefreshTokenDto rotate(String tokenString) {
        String tokenHash = DigestUtils.sha256Hex(tokenString);
        RefreshToken token = tokenRepository.findByToken(tokenHash)
                .orElseThrow(() -> new TokenNotFoundException("Invalid or expired refresh token. Please log in again."));

        // Mark the current refresh token as used to prevent reuse (token rotation)
        token.setUsedAt(Instant.now());

        // Generate a new refresh token and hash it before storing
        String newTokenString = UUID.randomUUID().toString();
        String newTokenHash = DigestUtils.sha256Hex(newTokenString);

        RefreshToken newToken = RefreshToken.builder()
                .token(newTokenHash)
                .session(token.getSession())
                .expiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
                .build();

        RefreshToken savedToken = tokenRepository.save(newToken);

        return convertTokenToDto(newTokenString, savedToken);
    }

    /**
     * Retrieves existing refresh token entry from DB.
     */
    @Override
    @Transactional(readOnly = true)
    public RefreshTokenDto getToken(String tokenString) {
        String tokenHash = DigestUtils.sha256Hex(tokenString);
        RefreshToken token = tokenRepository.findByToken(tokenHash)
                .orElseThrow(() -> new TokenNotFoundException(
                        "Your session is no longer valid. Please log in again.",
                        ErrorCode.AUTH_SESSION_INVALID
                ));

        return convertTokenToDto(tokenString, token);
    }

    /**
     * Validates refresh token status and expiration.
     */
    @Override
    public void validateToken(RefreshTokenDto token) {
        // Reuse detected
        if (token.getRevokedAt() != null || token.getUsedAt() != null) {
            throw new TokenRevokedException(
                    "Your session is no longer valid. Please log in again.",
                    ErrorCode.AUTH_SESSION_INVALID
            );
        }

        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new TokenExpiredException(
                    "Your session has expired due to inactivity. Please log in again.",
                    ErrorCode.AUTH_SESSION_EXPIRED
            );
        }
    }

    /**
     * Maps domain RefreshToken entity to DTO.
     */
    private RefreshTokenDto convertTokenToDto(String tokenString, RefreshToken token) {
        return RefreshTokenDto.builder()
                .id(token.getId())
                .token(tokenString)
                .sessionId(token.getSession().getId())
                .userId(token.getSession().getUserId())
                .usedAt(token.getUsedAt())
                .revokedAt(token.getRevokedAt())
                .expiresAt(token.getExpiresAt())
                .build();
    }
}