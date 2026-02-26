package com.notif.api.auth.infrastructure.repository;

import com.notif.api.auth.domain.model.RefreshToken;
import com.notif.api.auth.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for RefreshToken, extending JpaRepository and RefreshTokenRepository.
 */
@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
}