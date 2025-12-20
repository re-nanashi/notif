package com.notif.api.user.exception;

import com.notif.api.common.exception.ResourceNotFoundException;

/**
 * Exception thrown when a requested user cannot be found.
 */
public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}