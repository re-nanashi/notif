package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.RefreshToken;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for RefreshToken, extending JpaRepository and RefreshTokenRepository.
 */
@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RefreshToken r WHERE r.token = :token")
    Optional<RefreshToken> findByTokenWithLock(@Param("token") String token);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE RefreshToken t
        SET t.revokedAt = CURRENT_TIMESTAMP
        WHERE t.session.userId = :userId
          AND t.revokedAt IS NULL
    """)
    int revokeTokensByUserId(@Param("userId") UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE RefreshToken t
        SET t.revokedAt = CURRENT_TIMESTAMP
        WHERE t.session.id = :sessionId
          AND t.revokedAt IS NULL
    """)
    int revokeTokensBySessionId(@Param("sessionId") UUID sessionId);
}