package com.notif.api.auth.application.service;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.infrastructure.security.JwtTokenProvider;
import com.notif.api.core.constants.AppConstants;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.client.UserClient;
import com.notif.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserClient userClient;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // TODO: refactor; userRepository uses UserDetails, just use the UserDetails do not worry about the jwt lol
    /**
    public AuthenticatedUserDTO getCurrentlyLoggedUser(
            HttpServletRequest request,
            HttpServletResponse response,
            UserDetails user
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final UserD userInfo;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("Missing or invalid Authorization header.");
        }

        jwtToken = authHeader.substring(7);
        userInfo = userClient.findByEmail(user.getUsername())
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + user.getUsername() + " does not exists.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        if (!jwtTokenProvider.isTokenValid(jwtToken, user)) {
            throw new AuthenticationException("Expired or invalid token.");
        }

        Date expiration = jwtTokenProvider.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        return AuthenticatedUserDTO.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .expiresIn(expiresIn)
                .build();
    }
     */

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
                .build();
    }

    // TODO: Add auth-specific fields to RegisterRequest (e.g., agreeToTerms, captchaToken) then validate
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
                .message("Action Required: Please verify your email to activate your account.")
                .build();
    }

    public RegisterResponse confirmRegistration(String token, String userEmail) {
        UserResponse activatedUser = userClient.enableUser(token, userEmail);

        return RegisterResponse.builder()
                .id(activatedUser.getId())
                .email(activatedUser.getEmail())
                .emailVerified(activatedUser.isEmailVerified())
                .fullName(activatedUser.getFullName())
                .role(activatedUser.getRole())
                .message("Email verified successfully. You can now log in to your account.")
                .build();
    }

    public RegisterResponse resendVerificationEmail(String userEmail) {
        UserResponse requestedUser = userClient.requestVerification(userEmail);

        return RegisterResponse.builder()
                .id(requestedUser.getId())
                .email(requestedUser.getEmail())
                .emailVerified(requestedUser.isEmailVerified())
                .fullName(requestedUser.getFullName())
                .role(requestedUser.getRole())
                .message("Action Required: Please verify your email to activate your account.")
                .build();
    }
}