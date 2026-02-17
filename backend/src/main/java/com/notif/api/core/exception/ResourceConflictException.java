package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException {
    private final ErrorCodes errorCode;

    public ResourceConflictException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}