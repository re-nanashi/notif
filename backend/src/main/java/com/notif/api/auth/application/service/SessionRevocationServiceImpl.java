package com.notif.api.auth.application.service;

import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import com.notif.api.auth.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementation for revoking sessions and associated refresh tokens.
 */
@Service
@RequiredArgsConstructor
public class SessionRevocationServiceImpl implements SessionRevocationService {
    private final SessionRepository sessionRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Revokes a specific session and its tokens.
     */
    @Override
    @Transactional
    public void revokeSession(UUID sessionId, SessionRevokedReason reason) {
        sessionRepository.revokeActiveSessionById(sessionId, reason);
        refreshTokenRepository.revokeTokensBySessionId(sessionId);
    }

    /**
     * Revokes all active sessions for a user and their tokens.
     */
    @Override
    @Transactional
    public void revokeAllUserSessions(UUID userId, SessionRevokedReason reason) {
        sessionRepository.revokeAllActiveSessionsByUserId(userId, reason);
        refreshTokenRepository.revokeTokensByUserId(userId);
    }

    /**
     * Revokes tokens for a session in an independent transaction.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeSessionTokens(UUID sessionId) {
        refreshTokenRepository.revokeTokensBySessionId(sessionId);
    }
}