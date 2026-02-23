package com.notif.api.user.application.service;

import com.notif.api.core.exception.*;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.UserRepository;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    public VerificationToken generateVerificationToken(String userEmail) {
        String token = UUID.randomUUID().toString();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + userEmail + " does not exists.",
                        ErrorCode.USER_NOT_FOUND
                ));

        VerificationToken tok = VerificationToken.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusMinutes(VerificationToken.EXPIRATION))
                .used(false)
                .build();

        return tokenRepository.save(tok);
    }

    public VerificationToken validateVerificationToken(String token, String userEmail) {
        // Check if token and user exists
        VerificationToken tok = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is either malformed or invalid.",
                        ErrorCode.USER_VERIFICATION_TOKEN_NOT_FOUND
                ));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + userEmail + " does not exists.",
                        ErrorCode.USER_NOT_FOUND
                ));

        // Check verification token validity
        if (!user.equals(tok.getUser())) {
            throw new ValidationException(
                    "Verification token user mismatch.",
                    ErrorCode.USER_VERIFICATION_TOKEN_INVALID
            );
        }
        if (tok.isUsed()) {
            throw new ConflictException(
                    "This account has already been verified. Please log in.",
                    ErrorCode.USER_VERIFICATION_TOKEN_ALREADY_USED
            );
        }
        if (tok.isTokenExpired()) {
            throw new UnauthorizedException(
                    "The verification link has expired. Please request a new one.",
                    ErrorCode.USER_VERIFICATION_TOKEN_EXPIRED
            );
        }

        tok.setUsed(true);

        return tokenRepository.save(tok);
    }
}