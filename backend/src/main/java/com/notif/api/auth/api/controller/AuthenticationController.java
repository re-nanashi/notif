package com.notif.api.auth.api.controller;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.service.AuthenticationService;
import com.notif.api.core.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String response =  "This endpoint is a not secure.";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentlyLoggedUserInfo(@AuthenticationPrincipal UserDetails loggedInUser) {
        CurrentlyLoggedInUserInfo userInfo = authenticationService.getCurrentlyLoggedUser(loggedInUser.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", userInfo));
    }

    // TODO: Properly structure response so user can get a hint to verify email.
    //  - appUrl should be frontend url and not backend.
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Registration successful", response));
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<ApiResponse> confirmRegistration(
            @RequestParam("token") String token,
            @RequestParam("email") String email
    ) {
        // TODO: Check if token is already used
        RegisterResponse response = authenticationService.confirmRegistration(token, email);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", response));
    }
}