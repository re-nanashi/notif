package com.notif.api.user.application.service;

import com.notif.api.user.domain.model.VerificationToken;

public interface VerificationTokenService {
    VerificationToken generateVerificationToken(String userEmail);
    VerificationToken validateVerificationToken(String token, String userEmail);
}