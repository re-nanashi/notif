package com.notif.api.user.application.service;

import com.notif.api.user.domain.model.User;

public interface VerificationTokenService {
    void createVerificationToken(String userEmail, String token);
    void validateVerificationToken(String token, String userEmail);
}