package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.AuthResult;

public interface AuthenticationService {
    CurrentlyLoggedInUserInfo getCurrentlyLoggedUser(String email);
    AuthResult authenticate(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    RegisterResponse confirmRegistration(String token, String userEmail);
    RegisterResponse resendVerificationEmail(String userEmail);
}