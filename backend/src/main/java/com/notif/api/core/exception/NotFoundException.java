package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message, ErrorCodes.NOT_FOUND);
    }

    public NotFoundException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }
}