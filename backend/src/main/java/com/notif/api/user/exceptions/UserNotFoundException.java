package com.notif.api.user.exceptions;

import com.notif.api.common.exception.ResourceNotFoundException;

/**
 * Exception thrown when a requested resource cannot be found.
 * Will be mapped to an HTTP 404 (Not Found) response.
 */
public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}