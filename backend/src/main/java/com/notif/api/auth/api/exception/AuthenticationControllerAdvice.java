package com.notif.api.auth.api.exception;

import com.notif.api.auth.api.controller.AuthenticationController;
import com.notif.api.auth.domain.exception.TokenExpiredException;
import com.notif.api.auth.domain.exception.TokenNotFoundException;
import com.notif.api.auth.domain.exception.TokenRevokedException;
import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;
import com.notif.api.core.dto.ApiError;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for AuthenticationController.
 *
 * Provides consistent and secure error responses for authentication-related failures.
 */
@RestControllerAdvice(assignableTypes = AuthenticationController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AuthenticationControllerAdvice {
    /**
     * Handles authentication failures caused by invalid credentials.
     * Triggered when the email does not exist or the password is incorrect.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCode.AUTH_INVALID_CREDENTIALS.getValue())
                .detail("Invalid email or password. Please try again or reset your password.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Handles cases where a user account exists but is disabled,
     * commonly due to unverified email or administrative action.
     * Returns HTTP 403 as authentication is valid but access is restricted.
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabled(DisabledException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.FORBIDDEN.getReasonPhrase())
                .status(HttpStatus.FORBIDDEN.value())
                .error(ErrorCode.USER_ACCOUNT_DISABLED.getValue())
                .detail("User account is disabled. Please verify your email.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Handles locked accounts, typically triggered by excessive failed
     * login attempts or security enforcement policies.
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiError> handleLocked(LockedException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.FORBIDDEN.getReasonPhrase())
                .status(HttpStatus.FORBIDDEN.value())
                .error(ErrorCode.USER_ACCOUNT_SUSPENDED.getValue())
                .detail("User account locked. Please contact your system administrator for assistance.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Handles expired user accounts.
     * Indicates that the account lifecycle has ended.
     */
    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiError> handleAccountExpiredException(AccountExpiredException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCode.USER_ACCOUNT_EXPIRED.getValue())
                .detail("User account has expired. Please contact your system administrator for assistance.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Handles expired user credentials (e.g., forced password reset policy).
     * Returns HTTP 401 and prompts the user to update credentials.
     */
    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ApiError> handleCredentialsExpired(CredentialsExpiredException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCode.USER_CREDENTIALS_EXPIRED.getValue())
                .detail("User credentials have expired. Please reset it immediately.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Handles refresh token and JWT token-related business exceptions
     * such as expired, missing, or revoked tokens.
     * Returns HTTP 401 to force re-authentication.
     */
    @ExceptionHandler({TokenExpiredException.class, TokenNotFoundException.class, TokenRevokedException.class})
    public ResponseEntity<ApiError> handleTokenExceptions(BusinessException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ex.getErrorCode().getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiError> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCode.UNAUTHORIZED.getValue())
                .detail("Refresh token cookie missing.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    /**
     * Fallback handler for unanticipated authentication-related exceptions
     * such as JWT parsing failures or generic AuthenticationException.
     * Returns a generic HTTP 401 response to prevent leaking internal details.
     */
    @ExceptionHandler({JwtException.class, AuthenticationException.class})
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCode.AUTHENTICATION_FAILED.getValue())
                .detail("Authentication failed. Please log in again or try again later.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}