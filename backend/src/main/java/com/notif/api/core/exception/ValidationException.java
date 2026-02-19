package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_FAILED);
    }

    public ValidationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}