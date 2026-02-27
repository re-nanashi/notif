package com.notif.api.user.domain.repository;

import com.notif.api.user.domain.model.TokenStatus;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;

import java.util.Optional;

/**
 * Repository interface for VerificationToken entity.
 */
public interface VerificationTokenRepository {
    VerificationToken save(VerificationToken token);
    Optional<VerificationToken> findByToken(String token);
    void voidPendingTokensByUser(User user, TokenStatus newStatus, TokenStatus currentStatus);
}