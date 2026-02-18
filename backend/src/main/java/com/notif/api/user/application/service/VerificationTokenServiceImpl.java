package com.notif.api.user.application.service;

import com.notif.api.core.exception.ErrorCodes;
import com.notif.api.core.exception.NotFoundException;
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
public class VerificationTokenServiceImpl {
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

    // TODO: Should we remove userEmail?
    public User validateVerificationToken(String token, String userEmail) {
        VerificationToken tok = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(
                        "Verification token is invalid.",
                        ErrorCodes.INVALID_TOKEN
                ));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + userEmail + " does not exists.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        if (!user.equals(tok.getUser())) {
            // TODO: exception
            throw new RuntimeException(ErrorCodes.INVALID_TOKEN.getValue());
        }
        if (tok.isTokenExpired()) {
            // TODO: exception
            throw new RuntimeException(ErrorCodes.TOKEN_EXPIRED.getValue());
        }

        user.setEnabled(true);

        return userRepository.save(user);
    }
}