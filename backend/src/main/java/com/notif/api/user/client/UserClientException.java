package com.notif.api.user.client;

import com.notif.api.core.exception.ErrorCodes;
import com.notif.api.core.exception.IntegrationException;
import lombok.Getter;

@Getter
public class UserClientException extends IntegrationException {
    public UserClientException(String message) {
        super(message, ErrorCodes.SERVICE_UNAVAILABLE);
    }

    public UserClientException(String message, ErrorCodes errorCode) {
        super(message, errorCode);
    }

    public UserClientException(String message, ErrorCodes errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}