package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.AuthenticationResult;
import com.notif.api.auth.application.dto.RefreshTokenDto;
import com.notif.api.auth.infrastructure.security.JwtTokenProvider;
import com.notif.api.auth.infrastructure.security.NotifUserDetails;
import com.notif.api.core.constants.AppConstants;
import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.api.dto.CreateUserRequest;
import com.notif.api.user.api.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Implementation of AuthenticationService that handles login, registration,
 * email verification, and token issuance. It authenticates users via Spring Security,
 * generates JWT access tokens, manages refresh tokens, and delegates user-related
 * operations to the User service through UserClient.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserClient userClient;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Registers a new user by delegating user creation to the User service.
     * Returns registration details and prompts email verification.
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // TODO: Add auth-specific fields to RegisterRequest (e.g., agreeToTerms, captchaToken) then validate
        // Map RegisterRequest to CreateUserRequest
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        UserResponse createdUser = userClient.createUser(createUserRequest);

        return mapUserToRegisterResponse(
                createdUser,
                "Action Required: Please verify your email to activate your account."
        );
    }

    /**
     * Confirms user registration by validating the verification token
     * and enabling the user account.
     */
    @Override
    public RegisterResponse confirmRegistration(String token, String userEmail) {
        UserResponse activatedUser = userClient.enableUser(token, userEmail);

        return mapUserToRegisterResponse(
                activatedUser,
                "Email verified successfully. You can now log in to your account."
        );
    }

    /**
     * Triggers re-sending of the email verification link
     * for users who have not yet verified their account.
     */
    @Override
    public RegisterResponse resendVerificationEmail(String userEmail) {
        UserResponse requestedUser = userClient.requestVerification(userEmail);

        return mapUserToRegisterResponse(
                requestedUser,
                "Action Required: Please verify your email to activate your account."
        );
    }

    /**
     * Helper function for mapping user information into response.
     */
    private RegisterResponse mapUserToRegisterResponse(UserResponse user, String message) {
        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .message(message)
                .build();
    }

    /**
     * Retrieves the currently authenticated user's profile information
     * from the User service using their email identifier.
     */
    @Override
    public AuthenticatedUserResponse getAuthenticatedUser(String email) {
        UserResponse user = userClient.getUserByEmail(email);

        return AuthenticatedUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    /**
     * Authenticates user credentials, generates a JWT access token and
     * refresh token, and returns both as part of the authentication result.
     */
    @Override
    public AuthenticationResult<LoginResponse> authenticate(LoginRequest request) {
        // Delegate credential validation to Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Extract authenticated user information then load full user profile
        NotifUserDetails userDetails = (NotifUserDetails)authentication.getPrincipal();
        AuthenticatedUserResponse userInfo = getAuthenticatedUser(userDetails.getUsername());

        // Issue short-lived JWT access token
        String jwtToken = jwtTokenProvider.generateToken(userDetails);
        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;
        // Issue long-lived refresh token
        // TODO: What should we do on existing refresh tokens?
        RefreshTokenDto refreshToken = refreshTokenService.generateRefreshToken(userDetails.getId());

        // Map login response
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(userInfo)
                .build();

        return new AuthenticationResult<>(loginResponse, refreshToken.getToken());
    }

    // TODO: rotate the refresh token (revoke old, issue new) for extra security; return an AuthenticationResult
    @Override
    public AuthenticationResult<LoginResponse> refresh(String refreshToken) {
        // Validate the refresh token
        RefreshTokenDto validatedToken = refreshTokenService.validateRefreshToken(refreshToken);

        // Extract associated user from token
        UserAuthDetails associatedUser = userClient.getUserAuthDetailsById(validatedToken.getUserId());
        NotifUserDetails userDetails = new NotifUserDetails(associatedUser);

        // Generate a new short-lived JWT access token via JwtService
        String jwtToken = jwtTokenProvider.generateToken(userDetails);
        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();

        return new AuthenticationResult<>(loginResponse, validatedToken.getToken());
    }

    @Override
    public AuthenticationResult<LogoutResponse> logout(String refreshToken) {
        if (refreshToken != null) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }

        return new AuthenticationResult<>(
                new LogoutResponse("Logged out successfully", LocalDateTime.now()),
                null
        );
    }
}