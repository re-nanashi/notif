package com.notif.api.auth.api.dto;

import com.notif.api.user.domain.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response object returned after a successful user registration.
 * Contains basic account information along with registration metadata
 * and a status message describing the result of the operation.
 */
@Data
@Builder
public class RegisterResponse {
    private UUID id;
    private String email;
    private boolean emailVerified;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
    private String message;
}