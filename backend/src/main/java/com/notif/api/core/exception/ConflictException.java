package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ConflictException extends BusinessException {
    public ConflictException(String message) {
        super(message, ErrorCode.RESOURCE_CONFLICT);
    }

    public ConflictException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}