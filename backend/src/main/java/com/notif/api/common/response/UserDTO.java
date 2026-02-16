package com.notif.api.user.dto;

import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring user profile data.
 *
 * Excludes sensitive fields such as passwords.
 * Used for responses to clients or other services.
 * TODO:
 *  - [ ] emailVerified
 *  - [ ] fullName; computed instead of stored
 *  - [ ] roles - List; e.g., ["USER"], ["ADMIN"]
 *  - [ ] status - Enum; e.g., ACTIVE, INACTIVE, SUSPENDED
 *  - [ ] createdAt
 *  - [ ] lastLogin
 */
@Data
public class UserDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
}