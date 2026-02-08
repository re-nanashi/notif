package com.notif.api.user.exception;

import com.notif.api.common.constants.ErrorCodes;
import lombok.Getter;

/**
 * Exception thrown when two password fields (e.g., password and confirm password) do not match during validation.
 */
@Getter
public class PasswordMismatchException extends RuntimeException {
    private final ErrorCodes errorCode;

    public PasswordMismatchException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}