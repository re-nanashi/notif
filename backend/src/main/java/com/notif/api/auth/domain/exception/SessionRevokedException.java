package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class SessionRevokedException extends BusinessException {
    public SessionRevokedException(String message) {
        super(message, ErrorCode.AUTH_SESSION_INVALID);
    }

    public SessionRevokedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}