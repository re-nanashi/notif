package com.notif.api.user.application.service;

import com.notif.api.core.domain.event.EventPublisher;
import com.notif.api.core.exception.*;
import com.notif.api.user.domain.event.VerificationRequestedEvent;
import com.notif.api.user.domain.model.TokenStatus;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.UserRepository;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EventPublisher eventPublisher;

    public VerificationToken generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken tok = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(VerificationToken.EXPIRATION))
                .status(TokenStatus.PENDING)
                .build();

        VerificationToken savedToken = tokenRepository.save(tok);

        eventPublisher.publish(new VerificationRequestedEvent(user.getEmail(), savedToken.getToken()));

        return tokenRepository.save(tok);
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public VerificationToken validateVerificationToken(String token, User user) {
        // Check if token and user exists
        VerificationToken tok = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is either malformed or invalid.",
                        ErrorCode.USER_VERIFICATION_TOKEN_NOT_FOUND
                ));

        // Check verification token validity
        if (!user.equals(tok.getUser())) {
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

        return tokenRepository.save(tok);
    }

    @Override
    public void voidExistingTokens(User user) {
        List<VerificationToken> activeTokens = tokenRepository.findByUserAndStatus(user, TokenStatus.PENDING);

        activeTokens.forEach(token -> {
            token.setStatus(TokenStatus.VOIDED);
            tokenRepository.save(token);
        });
    }
}