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
import org.springframework.transaction.annotation.Propagation;
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
        String tokenString = UUID.randomUUID().toString();
        User userProxy = entityManager.getReference(User.class, userId);

        VerificationToken token = VerificationToken.builder()
                .token(tokenString)
                .user(userProxy)
                .expiresAt(LocalDateTime.now().plusMinutes(VerificationToken.EXPIRATION))
                .status(TokenStatus.PENDING)
                .build();

        VerificationToken savedToken = tokenRepository.save(token);

        // Publish event for async email notification
        eventPublisher.publish(new VerificationRequestedEvent(userProxy.getId(), savedToken.getToken()));
    }

    /**
     * Validates verification tokens and updates token status accordingly.
     */
    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            noRollbackFor = UnauthorizedException.class
    )
    public void validateVerificationToken(String tokenString, UUID userId) {
        // Check if token exists
        VerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is either malformed or invalid.",
                        ErrorCode.USER_VERIFICATION_TOKEN_NOT_FOUND
                ));

        // Check token validity
        if (!userId.equals(token.getUser().getId())) {
            throw new ValidationException(
                    "Verification token user mismatch.",
                    ErrorCode.USER_VERIFICATION_TOKEN_INVALID
            );
        }
        if (token.getStatus() == TokenStatus.VOIDED) {
            throw new ValidationException(
                    "This verification link is no longer valid because a newer one was requested.",
                    ErrorCode.USER_VERIFICATION_TOKEN_INVALID
            );
        }
        if (token.getStatus() == TokenStatus.VERIFIED) {
            throw new ConflictException(
                    "This account has already been verified. Please log in.",
                    ErrorCode.USER_VERIFICATION_TOKEN_ALREADY_USED
            );
        }
        if (token.isTokenExpired()) {
            token.setStatus(TokenStatus.EXPIRED); // set status to expired; to be deleted by batch

            throw new UnauthorizedException(
                    "The verification link has expired. Please request a new one.",
                    ErrorCode.USER_VERIFICATION_TOKEN_EXPIRED
            );
        }
    }

    /**
     * Consumes verification token (set status to TokenStatus.VERIFIED)
     */
    @Override
    @Transactional
    public void consumeToken(String tokenString) {
        VerificationToken token = tokenRepository.findByToken(tokenString + "1")
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is either malformed or invalid.",
                        ErrorCode.USER_VERIFICATION_TOKEN_NOT_FOUND
                ));

        token.setStatus(TokenStatus.VERIFIED);
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