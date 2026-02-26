package com.notif.api.user.api.dto;

import com.notif.api.user.domain.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Security DTO that carries user authentication and authorization data.
 * Used to construct security principal objects without exposing persistence entities.
 */
@Data
@Builder
public class UserAuthDetails {
    private UUID id;
    private String email;
    private String password;
    private Role role;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
}