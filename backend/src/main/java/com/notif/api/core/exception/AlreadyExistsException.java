package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends BusinessException {
    public AlreadyExistsException(String message) {
        super(message, ErrorCodes.ALREADY_EXISTS);
    }

    public AlreadyExistsException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}