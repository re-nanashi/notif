package com.notif.api.user.application.service;

import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.model.VerificationToken;

public interface VerificationTokenService {
    VerificationToken generateVerificationToken(User user);
    VerificationToken validateVerificationToken(String token, User user);
    void voidExistingTokens(User user);
}