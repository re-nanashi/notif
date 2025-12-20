package com.notif.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for changing a user's password.
 *
 * Contains the current password, the new password, and a confirmation password
 * to ensure the user entered the intended new password.
 *
 * Validation rules:
 * - {@code currentPassword} must not be blank
 * - {@code newPassword} must not be blank, between 8-64 characters, and contain
 *   at least one uppercase letter, one lowercase letter, and one number
 * - {@code confirmationPassword} must not be blank
 */
@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,64}$",
            message = "Password must contain uppercase, lowercase, and a number")
    private String newPassword;

    /** Confirmation of the new password. Must match {@code newPassword}. */
    @NotBlank(message = "Confirmation password is required")
    private String confirmationPassword;
}