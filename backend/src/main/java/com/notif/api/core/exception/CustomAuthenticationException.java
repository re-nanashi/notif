package com.notif.api.core.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;

    public CustomAuthenticationException(String message) {
        super(message);
        this.errorCode = ErrorCode.AUTHENTICATION_FAILED;
    }

    public CustomAuthenticationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomAuthenticationException(String message, ErrorCode errorCode, Throwable ex) {
        super(message, ex);
        this.errorCode = errorCode;
    }
}