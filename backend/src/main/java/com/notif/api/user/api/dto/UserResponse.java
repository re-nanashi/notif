package com.notif.api.user.api.dto;

import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring user profile data.
 *
 * Excludes sensitive fields such as passwords.
 * Used for responses to clients or other services.
 * TODO:
 *  - [ ] roles - List; e.g., ["USER"], ["ADMIN"]
 *  - [ ] status - Enum; e.g., ACTIVE, INACTIVE, SUSPENDED
 *  - [ ] createdAt
 *  - [ ] lastLogin
 */
@Data
public class UserResponse {
    private UUID id;
    private String email;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String fullName;
    private String role;
}