package com.notif.api.auth.domain.exception;

import com.notif.api.core.exception.BusinessException;
import com.notif.api.core.exception.ErrorCode;

public class TokenNotFoundException extends BusinessException {
    public TokenNotFoundException(String message) {
        super(message, ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND);
    }

    public TokenNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}