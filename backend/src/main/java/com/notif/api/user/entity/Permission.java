package com.notif.api.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the different permissions available in the system.
 *
 * Each constant maps to a specific permission string (e.g., "admin:read"),
 * which can be used in authorization checks.
 */
@RequiredArgsConstructor
public enum Permission {
    // Admin permissions
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    // Manager permissions
    MANAGER_READ("manager:read"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_CREATE("manager:create"),
    MANAGER_DELETE("manager:delete");

    /**
     * The permission string value (e.g., "admin:read").
     */
    @Getter
    private final String permission;
}