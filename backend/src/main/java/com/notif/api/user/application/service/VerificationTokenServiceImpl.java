package com.notif.api.user.application.service;

import com.notif.api.core.domain.event.EventPublisher;
import com.notif.api.core.exception.*;
import com.notif.api.user.domain.event.VerificationRequestedEvent;
import com.notif.api.user.domain.model.TokenStatus;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service responsible for verification token lifecycle management.
 * Handles token generation, validation, and invalidation workflows.
 */
@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;
    private final EntityManager entityManager;
    private final EventPublisher eventPublisher;

    /**
     * Generates a new verification token for user verification workflows.
     * Publishes verification request event after token persistence.
     */
    @Override
    @Transactional
    public void generateVerificationToken(UUID userId) {
        String token = UUID.randomUUID().toString();
        User userProxy = entityManager.getReference(User.class, userId);

        VerificationToken tok = VerificationToken.builder()
                .token(token)
                .user(userProxy)
                .expiresAt(LocalDateTime.now().plusMinutes(VerificationToken.EXPIRATION))
                .status(TokenStatus.PENDING)
                .build();

        VerificationToken savedToken = tokenRepository.save(tok);

        // Publish event for async email notification
        eventPublisher.publish(new VerificationRequestedEvent(userProxy.getId(), savedToken.getToken()));
    }

    /**
     * Validates verification tokens and updates token status accordingly.
     */
    @Override
    @Transactional(noRollbackFor = UnauthorizedException.class)
    public void validateVerificationToken(String token, UUID userId) {
        // Check if token and user exists
        VerificationToken tok = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is either malformed or invalid.",
                        ErrorCode.USER_VERIFICATION_TOKEN_NOT_FOUND
                ));

        // Check token validity
        if (!userId.equals(tok.getUser().getId())) {
            throw new ValidationException(
                    "Verification token user mismatch.",
                    ErrorCode.USER_VERIFICATION_TOKEN_INVALID
            );
        }
        if (tok.getStatus() == TokenStatus.VOIDED) {
            throw new ValidationException(
                    "This verification link is no longer valid because a newer one was requested.",
                    ErrorCode.USER_VERIFICATION_TOKEN_INVALID
            );
        }
        if (tok.getStatus() == TokenStatus.VERIFIED) {
            throw new ConflictException(
                    "This account has already been verified. Please log in.",
                    ErrorCode.USER_VERIFICATION_TOKEN_ALREADY_USED
            );
        }
        if (tok.isTokenExpired()) {
            tok.setStatus(TokenStatus.EXPIRED); // set status to expired; to be deleted by batch
            tokenRepository.save(tok);          // this will persist

            throw new UnauthorizedException(
                    "The verification link has expired. Please request a new one.",
                    ErrorCode.USER_VERIFICATION_TOKEN_EXPIRED
            );
        }

        tok.setStatus(TokenStatus.VERIFIED);

        tokenRepository.save(tok);
    }

    /**
     * Voids all pending verification tokens for a user. Prevents reuse of previously issued verification links.
     */
    @Override
    @Transactional
    public void voidPendingTokensByUserId(UUID userId) {
        User userProxy = entityManager.getReference(User.class, userId);
        tokenRepository.voidPendingTokensByUser(userProxy, TokenStatus.VOIDED, TokenStatus.PENDING);
    }
}