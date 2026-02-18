package com.notif.api.user.domain.exception;

import com.notif.api.core.exception.BusinessException;
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
public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(message, ErrorCodes.INVALID_CREDENTIALS);
    }

    public InvalidPasswordException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}