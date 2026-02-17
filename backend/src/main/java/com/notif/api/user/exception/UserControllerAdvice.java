package com.notif.api.user.exception;

import com.notif.api.core.exception.ErrorCodes;
import com.notif.api.common.response.ApiError;
import com.notif.api.user.controller.UserController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handles user-related exceptions thrown by {@link UserController}.
 * Maps exceptions to {@link ApiError} responses.
 */
@RestControllerAdvice(assignableTypes = UserController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserControllerAdvice {
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(InvalidPasswordException ex) {
        ApiError error = ApiError.builder()
                .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ErrorCodes.INVALID_CREDENTIALS.getValue())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}