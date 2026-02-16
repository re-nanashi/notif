package com.notif.api.auth.service;

import com.notif.api.auth.dto.AuthenticatedUserDTO;
import com.notif.api.auth.dto.AuthenticationRequest;
import com.notif.api.auth.dto.AuthenticationResponse;
import com.notif.api.auth.request.RegisterRequest;
import com.notif.api.common.constants.AppConstants;
import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.contracts.UserManagementContract;
import com.notif.api.common.exception.ResourceConflictException;
import com.notif.api.common.exception.ResourceNotFoundException;
import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;
import com.notif.api.config.security.JwtService;
import com.notif.api.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserManagementContract userManagement;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticatedUserDTO getCurrentlyLoggedUser(
            HttpServletRequest request,
            HttpServletResponse response,
            UserDetails user
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final User userInfo;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("Missing or invalid Authorization header.");
        }

        jwtToken = authHeader.substring(7);
        userInfo = userRepository.findByEmail(user.getUsername())
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

    public UserDTO register(RegisterRequest request) {
        // Check if user already exists
        if (userManagement.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(
                    "User with email '" + request.getEmail() + "' already exists.",
                    ErrorCodes.USER_ALREADY_EXISTS
            );
        }

        // Create user via contract
        return userManagement.createUser(
                CreateUserRequest.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .confirmPassword(request.getConfirmPassword())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .build()
        );
    }
}