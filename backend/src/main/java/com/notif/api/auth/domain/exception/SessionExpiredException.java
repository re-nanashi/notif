package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class SessionExpiredException extends BusinessException {
    public SessionExpiredException(String message) {
        super(message, ErrorCode.AUTH_SESSION_EXPIRED);
    }

    public SessionExpiredException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}