package com.notif.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO used for creating a new user account.
 *
 * Contains all required fields for registration.
 */
@Data
public class CreateUserRequest {
    // Account + login
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // TODO (Authentication): Check if we're going to use this DTO to create a user (init password) when registering through OAuth
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    // User profile
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}