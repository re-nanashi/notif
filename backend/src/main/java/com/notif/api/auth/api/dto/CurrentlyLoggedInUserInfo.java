package com.notif.api.auth.api.dto;

import com.notif.api.user.domain.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring user profile data of currently logged-in user.
 *
 * Excludes sensitive fields such as passwords.
 * Used for responses to clients or other services.
 */
@Data
@Builder
public class CurrentlyLoggedInUserInfo {
    private UUID id;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
}