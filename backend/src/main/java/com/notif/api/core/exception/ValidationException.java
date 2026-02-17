package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final ErrorCodes errorCode;

    public ValidationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}