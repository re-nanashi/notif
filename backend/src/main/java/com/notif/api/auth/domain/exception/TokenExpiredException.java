package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class TokenExpiredException extends BusinessException {
    public TokenExpiredException(String message) {
        super(message, ErrorCode.AUTH_REFRESH_TOKEN_EXPIRED);
    }

    public TokenExpiredException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}