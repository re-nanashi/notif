package com.notif.api.user.exception;

import com.notif.api.common.exception.AlreadyExistsException;

/**
 * Exception thrown when attempting to create a user that already exists in the system.
 */
public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}