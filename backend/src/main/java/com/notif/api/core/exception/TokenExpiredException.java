package com.notif.api.core.exception;

public class TokenExpiredException extends BusinessException {
    public TokenExpiredException(String message) {
        super(message, ErrorCodes.TOKEN_EXPIRED);
    }

    public TokenExpiredException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}