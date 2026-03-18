package com.notif.api.auth.application.service;

import com.notif.api.auth.application.dto.SessionDto;
import com.notif.api.auth.domain.exception.SessionExpiredException;
import com.notif.api.auth.domain.exception.SessionNotFoundException;
import com.notif.api.auth.domain.exception.SessionRevokedException;
import com.notif.api.auth.domain.model.Device;
import com.notif.api.auth.domain.model.Session;
import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.domain.model.SessionStatus;
import com.notif.api.auth.domain.repository.SessionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for managing session lifecycle, validation, and retrieval.
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRevocationService sessionRevocationService;
    private final SessionRepository sessionRepository;
    private final EntityManager entityManager;

    /**
     * Creates a new session for a user on a specific device.
     */
    @Override
    @Transactional
    public SessionDto createSession(UUID userId, UUID deviceId, String ipAddress) {
        Device deviceProxy = entityManager.getReference(Device.class, deviceId);

        Session newSession = Session.builder()
                .userId(userId)
                .device(deviceProxy)
                .ipAddress(ipAddress)
                .revokedAt(null)
                .revokedReason(null)
                .expiresAt(Instant.now().plus(Duration.ofDays(Session.MAX_LIFETIME)))
                .build();

        Session savedSession = sessionRepository.save(newSession);

        return mapSessionToDto(savedSession);
    }

    /**
     * Retrieves and validates an active session.
     * Updates last activity timestamp if valid.
     */
    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = SessionExpiredException.class
    )
    public SessionDto getActiveSession(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Your session is no longer valid. Please log in again."));

        Instant now = Instant.now();

        validateSession(session, now);
        session.setLastActivityAt(now);

        return mapSessionToDto(session);
    }

    /**
     * Validates session state and handles expiration logic.
     */
    private void validateSession(Session session, Instant now) {
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new SessionRevokedException("Your session is no longer valid. Please log in again.");
        }

        if (now.isAfter(session.getExpiresAt())) {
            // Terminate session due to absolute lifetime expiry
            session.setStatus(SessionStatus.EXPIRED);
            session.setRevokedReason(SessionRevokedReason.ABSOLUTE_EXPIRATION);

            // Revoke tokens tied to session; Runs independently
            sessionRevocationService.revokeSessionTokens(session.getId());

            throw new SessionExpiredException("Your session has expired. Please log in again.");
        }
    }

    /**
     * Retrieves an active session for a given device, if present.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SessionDto> getActiveSessionOnDevice(UUID deviceId) {
        return sessionRepository.findByDeviceIdAndStatus(deviceId, SessionStatus.ACTIVE)
                .map(this::mapSessionToDto);
    }

    /**
     * Maps Session entity to SessionDto.
     */
    private SessionDto mapSessionToDto(Session session) {
        return SessionDto.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .deviceId(session.getDevice().getId())
                .ipAddress(session.getIpAddress())
                .status(session.getStatus())
                .revokedAt(session.getRevokedAt())
                .revokedReason(session.getRevokedReason())
                .lastActivityAt(session.getLastActivityAt())
                .expiresAt(session.getExpiresAt())
                .build();
    }
}