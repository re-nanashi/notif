package com.notif.api.common.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * Will be mapped to an HTTP 404 (Not Found) response.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}