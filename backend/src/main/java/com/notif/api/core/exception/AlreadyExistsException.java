package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXISTS);
    }

    public AlreadyExistsException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}