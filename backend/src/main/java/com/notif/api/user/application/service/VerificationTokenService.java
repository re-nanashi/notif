package com.notif.api.user.application.service;

import java.util.UUID;

public interface VerificationTokenService {
    void generateVerificationToken(UUID userId);
    void validateVerificationToken(String token, UUID userId);
    void voidPendingTokensByUserId(UUID userId);
}