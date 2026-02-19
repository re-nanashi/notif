package com.notif.api.core.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}