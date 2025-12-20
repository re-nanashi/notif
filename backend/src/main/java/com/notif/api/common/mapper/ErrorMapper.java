package com.notif.api.common.mapper;

import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.exception.AlreadyExistsException;
import com.notif.api.common.exception.ResourceConflictException;
import com.notif.api.common.exception.ResourceNotFoundException;
import com.notif.api.common.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class ErrorMapper {
    public ApiError mapException(Exception ex) {
        return switch (ex) {
            case ResourceNotFoundException resourceNotFoundException -> ApiError.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .error(ErrorCodes.RESOURCE_NOT_FOUND.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case AlreadyExistsException alreadyExistsException -> ApiError.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error(ErrorCodes.RESOURCE_ALREADY_EXISTS.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case ResourceConflictException resourceConflictException -> ApiError.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error(ErrorCodes.RESOURCE_CONFLICT.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case IllegalStateException illegalStateException -> ApiError.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error(ErrorCodes.ILLEGAL_STATE.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case BadCredentialsException badCredentialsException -> ApiError.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .error(ErrorCodes.AUTHENTICATION_FAILED.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            // Generic fallback
            default -> ApiError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(ErrorCodes.INTERNAL_SERVER_ERROR.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
        };
    }

    public ApiError mapException(Exception ex, List<String> errors) {
        return switch (ex) {
            case MethodArgumentNotValidException methodArgumentNotValidException -> ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(ErrorCodes.VALIDATION_FAILED.getValue())
                    .messages(errors)
                    .timestamp(LocalDateTime.now())
                    .build();
            default -> ApiError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(ErrorCodes.INTERNAL_SERVER_ERROR.getValue())
                    .messages(errors)
                    .timestamp(LocalDateTime.now())
                    .build();
        };
    }
}