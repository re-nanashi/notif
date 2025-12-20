package com.notif.api.user.exception;

import com.notif.api.common.response.ApiError;
import com.notif.api.user.controller.UserController;
import com.notif.api.user.mapper.UserErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles user-related exceptions thrown by {@link UserController}.
 * Maps exceptions to {@link ApiError} responses.
 */
@RestControllerAdvice(assignableTypes = UserController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserControllerAdvice {
    @Autowired
    private UserErrorMapper errorMapper;

    @ExceptionHandler({PasswordMismatchException.class, UserNotFoundException.class, UserAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleUserCustomExceptions(RuntimeException ex) {
        ApiError apiError = errorMapper.mapException(ex);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}