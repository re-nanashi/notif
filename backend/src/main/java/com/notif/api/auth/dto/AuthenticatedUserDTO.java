package com.notif.api.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring user profile data of currently logged-in user.
 *
 * Excludes sensitive fields such as passwords.
 * Used for responses to clients or other services.
 */
@Data
@Builder
public class AuthenticatedUserDTO {
    private UUID id;
    private String email;
    // TODO: Roles, RefreshTokenExpiration
    private long expiresIn; // in seconds
}