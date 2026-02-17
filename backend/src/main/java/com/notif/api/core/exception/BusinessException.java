package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCodes errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = ErrorCodes.BUSINESS_ERROR;
    }

    public BusinessException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
