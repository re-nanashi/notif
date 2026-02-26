package com.notif.api.auth.infrastructure.security;

import com.notif.api.core.exception.CustomAuthenticationException;
import com.notif.api.core.exception.ErrorCode;
import org.springframework.stereotype.Component;

/**
 * Validates user access status before allowing authentication or protected resource access.
 *
 * Ensures that user account security states such as enabled status, account lock status, expiration, and
 * credential validity are checked, even if JWT is valid.
 */
@Component
public class UserAccessValidator {
    public void validateUserAccess(NotifUserDetails user) {
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