package com.notif.api.auth.api.controller;

import com.notif.api.auth.api.dto.*;
import com.notif.api.auth.application.dto.AuthenticationRequestContext;
import com.notif.api.auth.application.dto.AuthenticationResult;
import com.notif.api.auth.application.service.AuthenticationService;
import com.notif.api.auth.infrastructure.security.CookieUtil;
import com.notif.api.auth.infrastructure.security.NotifUserDetails;
import com.notif.api.core.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String response =  "This endpoint is a not secure.";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @NonNull HttpServletRequest servletRequest,
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        // Capture client metadata for authentication and session validation
        AuthenticationRequestContext context = AuthenticationRequestContext.builder()
                .userAgent(servletRequest.getHeader("User-Agent"))
                .clientIp(servletRequest.getRemoteAddr())
                .deviceId(CookieUtil.getCookieValue(servletRequest, CookieUtil.DEVICE_COOKIE_NAME))
                .build();

        AuthenticationResult<LoginResponse> result = authenticationService.authenticate(loginRequest, context);
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(result.getCookies().getRefreshToken());
        ResponseCookie deviceIdCookie = CookieUtil.createDeviceIdCookie(result.getCookies().getDeviceId());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, deviceIdCookie.toString())
                .body(new ApiResponse<>("Login successful", result.getResponse()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @NonNull HttpServletRequest servletRequest,
            @CookieValue(name = CookieUtil.REFRESH_COOKIE_NAME) String refreshToken
    ) {
        AuthenticationRequestContext context = AuthenticationRequestContext.builder()
                .deviceId(CookieUtil.getCookieValue(servletRequest, CookieUtil.DEVICE_COOKIE_NAME))
                .build();

        AuthenticationResult<LoginResponse> result = authenticationService.refresh(refreshToken, context);
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(result.getCookies().getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new ApiResponse<>("Success", result.getResponse()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(
            @NonNull HttpServletRequest servletRequest,
            @CookieValue(name = CookieUtil.REFRESH_COOKIE_NAME, required = false) String refreshToken
    ) {
        AuthenticationRequestContext context = AuthenticationRequestContext.builder()
                .deviceId(CookieUtil.getCookieValue(servletRequest, CookieUtil.DEVICE_COOKIE_NAME))
                .build();

        AuthenticationResult<LogoutResponse> result = authenticationService.logout(refreshToken, context);
        ResponseCookie cookie = CookieUtil.clearRefreshTokenCookie();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>("Success", result.getResponse()));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<LogoutResponse>> logoutAllDevice(
            @NonNull HttpServletRequest servletRequest,
            @CookieValue(name = CookieUtil.REFRESH_COOKIE_NAME, required = false) String refreshToken
    ) {
        AuthenticationRequestContext context = AuthenticationRequestContext.builder()
                .deviceId(CookieUtil.getCookieValue(servletRequest, CookieUtil.DEVICE_COOKIE_NAME))
                .build();

        AuthenticationResult<LogoutResponse> result = authenticationService.logoutAllDevices(refreshToken, context);
        ResponseCookie cookie = CookieUtil.clearRefreshTokenCookie();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>("Success", result.getResponse()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthenticatedUserResponse>> getAuthenticatedUser(
            @AuthenticationPrincipal NotifUserDetails user
    ) {
        AuthenticatedUserResponse userInfo = authenticationService.getAuthenticatedUser(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", userInfo));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Registration successful", response));
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<ApiResponse<RegisterResponse>> confirmRegistration(
            @RequestParam("token") String token,
            @RequestParam("email") String email
    ) {
        RegisterResponse response = authenticationService.confirmRegistration(token, email);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", response));
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<ApiResponse<RegisterResponse>> resendConfirmation(@RequestParam("email") String email) {
        RegisterResponse response = authenticationService.resendVerificationEmail(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>("Success", response));
    }
}