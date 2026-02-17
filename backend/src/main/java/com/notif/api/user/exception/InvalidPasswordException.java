package com.notif.api.user.exception;

import com.notif.api.core.exception.ErrorCodes;
import lombok.Getter;

/**
 * Thrown when a password validation fails during sensitive operations (e.g., email change).
 *
 * This is mapped to a 400 Bad Request rather than 401 Unauthorized to prevent
 * frontend interceptors from incorrectly triggering a global logout or session clearance
 * when the user simply provides an incorrect current password.
 */
@Getter
public class InvalidPasswordException extends RuntimeException {
    private final ErrorCodes errorCode;

    public InvalidPasswordException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}