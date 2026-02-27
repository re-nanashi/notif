package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.RefreshToken;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for RefreshToken, extending JpaRepository and RefreshTokenRepository.
 */
@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.userId = :userId AND t.revoked = false")
    void revokeAllByUserId(@Param("userId") UUID userId);
}