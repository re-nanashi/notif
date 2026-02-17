package com.notif.api.core.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, ErrorCodes.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}