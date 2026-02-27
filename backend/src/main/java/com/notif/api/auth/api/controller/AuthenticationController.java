package com.notif.api.auth.api.controller;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.AuthResult;
import com.notif.api.auth.application.service.AuthenticationService;
import com.notif.api.auth.application.service.RefreshTokenService;
import com.notif.api.auth.infrastructure.security.CookieUtil;
import com.notif.api.auth.infrastructure.security.NotifUserDetails;
import com.notif.api.core.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String response =  "This endpoint is a not secure.";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResult result = authenticationService.authenticate(request);
        ResponseCookie cookie = CookieUtil.createRefreshTokenCookie(result.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Login successful", result.getResponse()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentlyLoggedUserInfo(@AuthenticationPrincipal NotifUserDetails loggedInUser) {
        CurrentlyLoggedInUserInfo userInfo = authenticationService.getCurrentlyLoggedUser(loggedInUser.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", userInfo));
    }

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
        RegisterResponse response = authenticationService.confirmRegistration(token, email);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", response));
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<ApiResponse> resendConfirmation(@RequestParam("email") String email) {
        RegisterResponse response = authenticationService.resendVerificationEmail(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("Success", response));
    }
}