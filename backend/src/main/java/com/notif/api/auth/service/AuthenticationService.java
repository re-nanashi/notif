package com.notif.api.auth.service;

import com.notif.api.auth.response.AuthenticatedUserDTO;
import com.notif.api.auth.request.AuthenticationRequest;
import com.notif.api.auth.response.AuthenticationResponse;
import com.notif.api.auth.request.RegisterRequest;
import com.notif.api.shared.constants.AppConstants;
import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.contracts.UserManagementContract;
import com.notif.api.common.events.UserRegisteredEvent;
import com.notif.api.common.exception.ResourceConflictException;
import com.notif.api.common.exception.ResourceNotFoundException;
import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;
import com.notif.api.config.security.JwtService;
import com.notif.api.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserManagementContract userManagement;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // TODO: refactor; userRepository uses UserDetails
    public AuthenticatedUserDTO getCurrentlyLoggedUser(
            HttpServletRequest request,
            HttpServletResponse response,
            UserDetails user
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final UserDTO userInfo;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("Missing or invalid Authorization header.");
        }

        jwtToken = authHeader.substring(7);
        userInfo = userManagement.findByEmail(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email " + user.getUsername() + " does not exists.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        if (!jwtService.isTokenValid(jwtToken, user)) {
            throw new AuthenticationException("Expired or invalid token.");
        }

        Date expiration = jwtService.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

        return AuthenticatedUserDTO.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .expiresIn(expiresIn)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.generateToken((User)authentication.getPrincipal());
            Date expiration = jwtService.extractExpiration(jwtToken);
            long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / AppConstants.MILLISECONDS_PER_SECOND;

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .tokenType("Bearer")
                    .expiresIn(expiresIn)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid user request.");
        }
    }

    // TODO: Add auth-specific fields to RegisterRequest (e.g., agreeToTerms, captchaToken) then validate
    public UserDTO register(RegisterRequest request, String appUrl) {
        // Check if user already exists
        if (userManagement.userAlreadyExists(request.getEmail())) {
            throw new ResourceConflictException(
                    "User with email '" + request.getEmail() + "' already exists.",
                    ErrorCodes.USER_ALREADY_EXISTS
            );
        }

        // Map register request to user creation request
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        // Create user via contract
        UserDTO newUser = userManagement.createUser(createUserRequest);

        // Publish event
        eventPublisher.publishEvent(new UserRegisteredEvent(newUser.getId(), newUser.getEmail(), appUrl));

        return newUser;
    }

    public UserDTO confirmRegistration(String token, String userEmail) {
        return userManagement.enableUser(token, userEmail);
    }
}