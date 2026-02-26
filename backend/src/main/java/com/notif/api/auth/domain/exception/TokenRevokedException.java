package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class TokenRevokedException extends BusinessException {
    public TokenRevokedException(String message) {
        super(message, ErrorCode.AUTH_REFRESH_TOKEN_REVOKED);
    }

    public TokenRevokedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}