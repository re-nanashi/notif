package com.notif.api.core.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}