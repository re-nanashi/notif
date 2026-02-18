package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(message, ErrorCodes.INVALID_TOKEN);
    }

    public InvalidTokenException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}