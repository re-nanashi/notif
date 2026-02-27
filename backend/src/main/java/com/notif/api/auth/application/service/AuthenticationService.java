package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.AuthenticationResult;

/**
 * Provides authentication and registration operations, including user login,
 * new account registration, email verification confirmation, resending
 * verification emails, and retrieving the currently authenticated user.
 */
public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);
    RegisterResponse confirmRegistration(String token, String userEmail);
    RegisterResponse resendVerificationEmail(String userEmail);
    AuthenticatedUserResponse getAuthenticatedUser(String email);
    AuthenticationResult<LoginResponse> authenticate(LoginRequest request);
    AuthenticationResult<LoginResponse> refresh(String refreshToken);
    AuthenticationResult<LogoutResponse> logout(String refreshToken);
}