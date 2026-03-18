package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class SessionNotFoundException extends BusinessException {
    public SessionNotFoundException(String message) {
        super(message, ErrorCode.AUTH_SESSION_INVALID);
    }

    public SessionNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
