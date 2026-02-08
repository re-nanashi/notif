package com.notif.api.auth.service;

import com.notif.api.auth.dto.AuthenticationRequest;
import com.notif.api.auth.dto.AuthenticationResponse;
import com.notif.api.auth.dto.RegisterRequest;
import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.exception.ResourceConflictException;
import com.notif.api.common.util.Util;
import com.notif.api.config.security.JwtService;
import com.notif.api.user.entity.User;
import com.notif.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken((User)authentication.getPrincipal());
        Date expiration = jwtService.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / Util.MILLISECONDS_PER_SECOND;

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .expiresIn(expiresIn)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(
                    "User with email '" + request.getEmail() + "' already exists",
                    ErrorCodes.USER_ALREADY_EXISTS
            );
        }

        User newUser = User.builder()
                .firstName(request.getFirstName().strip()) // remove leading/trailing whitespaces
                .lastName(request.getLastName().strip())
                .email(request.getEmail()) // @Email annotation fails whitespaces
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();
        User savedUser = userRepository.save(newUser);

        String jwtToken = jwtService.generateToken(savedUser);
        Date expiration = jwtService.extractExpiration(jwtToken);
        long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / Util.MILLISECONDS_PER_SECOND;

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .expiresIn(expiresIn)
                .build();
    }
}