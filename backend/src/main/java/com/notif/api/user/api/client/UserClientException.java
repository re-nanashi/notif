package com.notif.api.user.api.client;

import com.notif.api.core.exception.ErrorCode;
import com.notif.api.core.exception.ModuleClientException;

public class UserClientException extends ModuleClientException {
    public UserClientException(String message) {
        super(message, ErrorCode.SERVICE_UNAVAILABLE);
    }

    public UserClientException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}