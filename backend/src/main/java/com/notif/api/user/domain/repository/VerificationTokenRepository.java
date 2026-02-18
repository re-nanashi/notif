package com.notif.api.user.domain.repository;

import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;

import java.util.Optional;

/**
 * Repository interface for VerificationToken entity.
 */
public interface VerificationTokenRepository {
    void save(VerificationToken token);
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
}
