package com.notif.api.auth.domain.model;

/**
 * Reasons for session termination (user action, security, or expiration).
 */
public enum SessionRevokedReason {
    LOGOUT,
    SESSION_REPLACED,
    TOKEN_REUSE,
    PASSWORD_CHANGE,
    ADMIN_REVOKED,
    USER_DELETED,
    IDLE_TIMEOUT,
    ABSOLUTE_EXPIRATION,
    DEVICE_MISMATCH,
    ANOMALOUS_ACTIVITY
}