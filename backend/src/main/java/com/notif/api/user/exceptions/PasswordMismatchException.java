package com.notif.api.user.exceptions;

/**
 * Thrown when two password fields (e.g., password and confirm password)
 * do not match during validation.
 */
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}