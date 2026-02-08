package com.notif.api.common.exception;

import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException {
    private final String errorCode;

    public ResourceConflictException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}