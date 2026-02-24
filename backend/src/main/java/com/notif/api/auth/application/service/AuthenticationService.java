package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;

public interface AuthenticationService {
    CurrentlyLoggedInUserInfo getCurrentlyLoggedUser(String email);
    LoginResponse authenticate(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    RegisterResponse confirmRegistration(String token, String userEmail);
    RegisterResponse resendVerificationEmail(String userEmail);
}