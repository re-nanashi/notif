package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}