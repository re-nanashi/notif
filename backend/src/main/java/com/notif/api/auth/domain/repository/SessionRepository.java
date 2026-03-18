package com.notif.api.auth.domain.repository;

import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.model.Session;
import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.domain.model.SessionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Session entity, defining core CRUD operations and session-specific queries.
 */
public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findById(UUID id);
    List<Session> findByUserId(UUID userId);
    List<Session> findByDevice(Device device);
    Optional<Session> findByUserIdAndDevice(UUID userId, Device device);
    Optional<Session> findByDeviceIdAndStatus(UUID deviceId, SessionStatus status);
    int revokeActiveSessionById(UUID sessionId, SessionRevokedReason revokedReason);
    int revokeAllActiveSessionsByUserId(UUID userId, SessionRevokedReason revokedReason);
}