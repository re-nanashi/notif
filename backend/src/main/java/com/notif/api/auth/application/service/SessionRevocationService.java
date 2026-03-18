package com.notif.api.auth.application.service;

import com.notif.api.auth.domain.model.SessionRevokedReason;

import java.util.UUID;

/**
 * Service interface for revoking sessions and its tokens.
 */
public interface SessionRevocationService {
    void revokeSession(UUID sessionId, SessionRevokedReason reason);
    void revokeAllUserSessions(UUID userId, SessionRevokedReason reason);
    void revokeSessionTokens(UUID sessionId);
}