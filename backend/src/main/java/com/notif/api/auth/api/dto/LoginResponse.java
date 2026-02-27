package com.notif.api.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication response DTO containing JWT token details
 * and information about the authenticated user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn; // token validity in seconds

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthenticatedUserResponse user;     // include only during initial login; not needed during refresh request
}