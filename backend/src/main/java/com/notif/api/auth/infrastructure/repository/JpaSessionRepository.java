package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.Session;
import com.notif.api.auth.domain.model.SessionRevokedReason;
import com.notif.api.auth.domain.repository.SessionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Spring Data JPA repository for Session, extending JpaRepository and SessionRepository.
 */
public interface JpaSessionRepository extends JpaRepository<Session, UUID>, SessionRepository {
    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Session s
        SET s.status = com.notif.api.auth.domain.model.SessionStatus.REVOKED,
            s.revokedAt = CURRENT_TIMESTAMP,
            s.revokedReason = :revokedReason
        WHERE s.id = :sessionId
          AND s.status = com.notif.api.auth.domain.model.SessionStatus.ACTIVE
    """)
    int revokeActiveSessionById(
            @Param("sessionId") UUID sessionId,
            @Param("revokedReason") SessionRevokedReason reason
    );

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Session s
        SET s.status = com.notif.api.auth.domain.model.SessionStatus.REVOKED,
            s.revokedAt = CURRENT_TIMESTAMP,
            s.revokedReason = :revokedReason
        WHERE s.userId = :userId
          AND s.status = com.notif.api.auth.domain.model.SessionStatus.ACTIVE
    """)
    int revokeAllActiveSessionsByUserId(
            @Param("userId") UUID userId,
            @Param("revokedReason") SessionRevokedReason reason
    );
}