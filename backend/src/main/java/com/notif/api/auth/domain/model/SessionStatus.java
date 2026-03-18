package com.notif.api.auth.domain.model;

/**
 * Represents the current state of a session.
 */
public enum SessionStatus {
    ACTIVE,         // Current session in use
    EXPIRED,        // Timed out automatically
    REVOKED         // Logout or Forcibly ended (security/admin control)
}