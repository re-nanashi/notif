package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.infrastructure.security.JwtTokenProvider;
import com.notif.api.core.constants.AppConstants;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.infrastructure.client.UserClient;
import com.notif.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserClient userClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public CurrentlyLoggedInUserInfo getCurrentlyLoggedUser(String email) {
        UserResponse user = userClient.getUserByEmail(email);

        return CurrentlyLoggedInUserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtTokenProvider.generateToken((User)authentication.getPrincipal());
        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(userInfo)
                .build();
    }

    // TODO: Add auth-specific fields to RegisterRequest (e.g., agreeToTerms, captchaToken) then validate
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Map RegisterRequest to CreateUserRequest
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        UserResponse createdUser = userClient.createUser(createUserRequest);

        return RegisterResponse.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .emailVerified(createdUser.isEmailVerified())
                .fullName(createdUser.getFullName())
                .role(createdUser.getRole())
                .createdAt(createdUser.getCreatedAt())
                .message("Action Required: Please verify your email to activate your account.")
                .build();
    }

    @Override
    public RegisterResponse confirmRegistration(String token, String userEmail) {
        UserResponse activatedUser = userClient.enableUser(token, userEmail);

        return RegisterResponse.builder()
                .id(activatedUser.getId())
                .email(activatedUser.getEmail())
                .emailVerified(activatedUser.isEmailVerified())
                .fullName(activatedUser.getFullName())
                .role(activatedUser.getRole())
                .createdAt(activatedUser.getCreatedAt())
                .message("Email verified successfully. You can now log in to your account.")
                .build();
    }

    @Override
    public RegisterResponse resendVerificationEmail(String userEmail) {
        UserResponse requestedUser = userClient.requestVerification(userEmail);

        return RegisterResponse.builder()
                .id(requestedUser.getId())
                .email(requestedUser.getEmail())
                .emailVerified(requestedUser.isEmailVerified())
                .fullName(requestedUser.getFullName())
                .role(requestedUser.getRole())
                .createdAt(requestedUser.getCreatedAt())
                .message("Action Required: Please verify your email to activate your account.")
                .build();
    }
}