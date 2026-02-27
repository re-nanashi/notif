package com.notif.api.auth.api.dto;

import com.notif.api.user.domain.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring user profile data of currently authenticated user.
 *
 * Excludes sensitive fields such as passwords.
 */
@Data
@Builder
public class AuthenticatedUserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private Role role;
}