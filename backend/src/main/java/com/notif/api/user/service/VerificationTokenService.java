package com.notif.api.user.service;

import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.exception.ResourceNotFoundException;
import com.notif.api.user.entity.User;
import com.notif.api.user.entity.VerificationToken;
import com.notif.api.user.repository.UserRepository;
import com.notif.api.user.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    public void createVerificationToken(String userEmail, String token) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Verification token is invalid.",
                        ErrorCodes.INVALID_TOKEN
                ));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
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