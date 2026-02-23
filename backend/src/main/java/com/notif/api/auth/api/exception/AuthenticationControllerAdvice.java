package com.notif.api.auth.api.exception;

import com.notif.api.auth.api.controller.AuthenticationController;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = AuthenticationController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AuthenticationControllerAdvice {
    // Handles wrong password or non-existent user
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

    // Handles disabled accounts (e.g., email not verified)
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

    // Handles locked accounts (e.g., too many failed attempts)
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

    // Handles expired account
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

    // Handles expired credentials
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

    // Fallback for all uncaught exceptions (including AuthenticationException, JwtException)
    @ExceptionHandler(Exception.class)
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