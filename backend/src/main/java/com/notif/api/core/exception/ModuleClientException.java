package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ModuleClientException extends RuntimeException {
    private final ErrorCode errorCode;

    public ModuleClientException(String message) {
        super(message);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
    }

    public ModuleClientException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}