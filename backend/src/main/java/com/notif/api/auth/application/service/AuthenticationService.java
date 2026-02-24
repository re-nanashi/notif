package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;

public interface AuthenticationService {
    public CurrentlyLoggedInUserInfo getCurrentlyLoggedUser(String email);
    public LoginResponse authenticate(LoginRequest request);
    public RegisterResponse register(RegisterRequest request);
    public RegisterResponse confirmRegistration(String token, String userEmail);
    public RegisterResponse resendVerificationEmail(String userEmail);
}