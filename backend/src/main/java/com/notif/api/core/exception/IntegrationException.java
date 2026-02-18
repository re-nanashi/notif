package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException {
    private final ErrorCodes errorCode;

    public IntegrationException(String message) {
        super(message);
        this.errorCode = ErrorCodes.INTEGRATION_ERROR;
    }

    public IntegrationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public IntegrationException(String message, ErrorCodes errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}