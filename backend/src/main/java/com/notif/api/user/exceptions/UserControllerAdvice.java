package com.notif.api.user.exceptions;

import com.notif.api.common.response.ApiError;
import com.notif.api.user.controller.UserController;
import com.notif.api.user.mapper.UserErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {
    @Autowired
    private UserErrorMapper errorMapper;

    @ExceptionHandler({PasswordMismatchException.class, UserNotFoundException.class, UserAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleUserSpecificExceptions(RuntimeException ex) {
        ApiError apiError = errorMapper.mapException(ex);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}