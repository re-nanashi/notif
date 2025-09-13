package com.notif.api.common.exception;

/**
 * Exception thrown when attempting to create a resource
 * that already exists in the system.
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}