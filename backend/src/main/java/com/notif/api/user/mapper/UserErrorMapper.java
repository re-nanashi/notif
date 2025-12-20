package com.notif.api.user.mapper;

import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.response.ApiError;
import com.notif.api.user.exception.PasswordMismatchException;
import com.notif.api.user.exception.UserAlreadyExistsException;
import com.notif.api.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Maps user-related exceptions to {@link ApiError} objects for API responses.
 */
@Component
public class UserErrorMapper {
    /**
     * Converts an exception to an {@link ApiError} suitable for returning in API responses.
     *
     * @param ex the exception to map
     * @return an {@link ApiError} containing HTTP status, error code, message(s), and timestamp
     */
    public ApiError mapException(Exception ex) {
        return switch (ex) {
            case PasswordMismatchException passwordMismatchException -> ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(ErrorCodes.PASSWORD_MISMATCH.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case UserNotFoundException userNotFoundException -> ApiError.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .error(ErrorCodes.USER_NOT_FOUND.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            case UserAlreadyExistsException userAlreadyExistsException -> ApiError.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error(ErrorCodes.USER_ALREADY_EXISTS.getValue())
                    .messages(Collections.singletonList(ex.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
            default ->
                // Generic fallback
                    ApiError.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .error(ErrorCodes.INTERNAL_SERVER_ERROR.getValue())
                            .messages(Collections.singletonList(ex.getMessage()))
                            .timestamp(LocalDateTime.now())
                            .build();
        };
    }
}