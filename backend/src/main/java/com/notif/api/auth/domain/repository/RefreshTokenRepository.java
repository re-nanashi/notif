package com.notif.api.auth.domain.repository;

import com.notif.api.auth.domain.model.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for RefreshToken entity.
 */
public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findAllByUserId(UUID userId);
    void deleteById(Long id);
}