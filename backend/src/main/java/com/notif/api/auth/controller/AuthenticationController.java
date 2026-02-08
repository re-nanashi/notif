package com.notif.api.auth.controller;

import com.notif.api.auth.dto.AuthenticationRequest;
import com.notif.api.auth.dto.AuthenticationResponse;
import com.notif.api.auth.dto.RegisterRequest;
import com.notif.api.auth.service.AuthenticationService;
import com.notif.api.common.response.ApiResponse;
import com.notif.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String response =  "This endpoint is a not secure";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentlyLoggedUserInfo(@AuthenticationPrincipal Authentication loggedInUser) {
        // TODO: We need JwtAuthenticationToken from OAuth2 to get the expiry details
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}