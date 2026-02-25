package com.notif.api.auth.api.dto;

import com.notif.api.user.domain.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

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
