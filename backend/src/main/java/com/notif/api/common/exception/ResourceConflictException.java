package com.notif.api.common.exception;

import com.notif.api.common.constants.ErrorCodes;
import lombok.Getter;

@Getter
public class ResourceConflictException extends RuntimeException {
    private final ErrorCodes errorCode;

    public ResourceConflictException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}