package com.notif.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for changing a user's email.
 *
 * Contains the new email to set and the user's current password for verification.
 * Validation annotations ensure:
 * - {@code newEmail} is not blank and is a valid email format
 * - {@code currentPassword} is not blank
 */
@Data
public class ChangeEmailRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String newEmail;

    @NotBlank(message = "Password is required")
    String currentPassword;
}