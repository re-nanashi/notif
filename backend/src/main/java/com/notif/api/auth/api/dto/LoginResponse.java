package com.notif.api.auth.api.dto;

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
    private CurrentlyLoggedInUserInfo user;
}