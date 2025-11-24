package com.notif.api.user.mapper;

import com.notif.api.common.response.ApiError;
import com.notif.api.user.exceptions.PasswordMismatchException;
import com.notif.api.user.exceptions.UserAlreadyExistsException;
import com.notif.api.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserErrorMapper {
    public ApiError mapException(Exception ex) {
        return switch (ex) {
            case PasswordMismatchException passwordMismatchException -> ApiError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error("Password Mismatch")
                    .errors(Collections.singletonList(ex.getMessage()))
                    .build();
            case UserNotFoundException userNotFoundException -> ApiError.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .error("User Not Found")
                    .errors(Collections.singletonList(ex.getMessage()))
                    .build();
            case UserAlreadyExistsException userAlreadyExistsException -> ApiError.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error("User Already Exists")
                    .errors(Collections.singletonList(ex.getMessage()))
                    .build();
            default ->
                // Generic fallback
                    ApiError.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .error("Internal Server Error")
                            .errors(Collections.singletonList(ex.getMessage()))
                            .build();
        };
    }
}