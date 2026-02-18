package com.notif.api.user.application.service;

import com.notif.api.core.exception.ErrorCodes;
import com.notif.api.core.exception.InvalidTokenException;
import com.notif.api.core.exception.NotFoundException;
import com.notif.api.core.exception.TokenExpiredException;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;
import com.notif.api.user.domain.repository.UserRepository;
import com.notif.api.user.domain.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    public void createVerificationToken(String userEmail, String token) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + userEmail + " does not exists.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        VerificationToken tok = VerificationToken.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusMinutes(VerificationToken.EXPIRATION))
                .build();

        tokenRepository.save(tok);
    }

    public void validateVerificationToken(String token, String userEmail) {
        // Check if token and user exists
        VerificationToken tok = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Verification token is invalid.", ErrorCodes.TOKEN_NOT_FOUND));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + userEmail + " does not exists.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        // Check token validity
        if (!user.equals(tok.getUser())) {
            throw new InvalidTokenException("Verification token user mismatch.", ErrorCodes.INVALID_TOKEN);
        }
        if (tok.isTokenExpired()) {
            throw new TokenExpiredException("Verification token has expired.", ErrorCodes.TOKEN_EXPIRED);
        }
    }
}