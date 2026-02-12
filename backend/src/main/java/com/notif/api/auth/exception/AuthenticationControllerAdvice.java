package com.notif.api.auth.exception;

import com.notif.api.auth.controller.AuthenticationController;
import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.response.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
                .error(ErrorCodes.INVALID_CREDENTIALS.getValue())
                .detail("Invalid username or password.")
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
                .error(ErrorCodes.ACCOUNT_DISABLED.getValue())
                .detail("Account is disabled. Please verify your email.")
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
                .error(ErrorCodes.ACCOUNT_LOCKED.getValue())
                .detail("Account is locked due to multiple failed attempts.")
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
                .error(ErrorCodes.PASSWORD_EXPIRED.getValue())
                .detail("Your password has expired. Please reset it.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    // Fallback for uncaught exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAllOtherAuthFailures(AuthenticationException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ErrorCodes.AUTHENTICATION_FAILED.getValue())
                .detail("Authentication failed.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}