package com.notif.api.auth.infrastructure.security;

import com.notif.api.core.exception.CustomAuthenticationException;
import com.notif.api.core.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserAccessValidator {
    public void validateUserAccess(UserDetails user) {
        if (!user.isEnabled()) {
            throw new CustomAuthenticationException(
                    "User account disabled. Please contact your system administrator for assistance.",
                    ErrorCode.USER_ACCOUNT_DISABLED
            );
        }
        if (!user.isAccountNonLocked()) {
            throw new CustomAuthenticationException(
                    "User account locked. Please contact your system administrator for assistance.",
                    ErrorCode.USER_ACCOUNT_SUSPENDED
            );
        }
        if (!user.isAccountNonExpired()) {
            throw new CustomAuthenticationException(
                    "User account has expired. Please contact your system administrator for assistance.",
                    ErrorCode.USER_ACCOUNT_EXPIRED
            );
        }
        if (!user.isCredentialsNonExpired()) {
            throw new CustomAuthenticationException(
                    "User credentials have expired. Please reset it immediately.",
                    ErrorCode.USER_CREDENTIALS_EXPIRED
            );
        }
    }
}