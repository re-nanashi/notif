package com.notif.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for changing a user's password.
 *
 * Contains the current password, the new password, and a confirmation
 * to ensure the user entered the intended new password.
 */
@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    @Size(min=8, max=64, message= "Password must be between 8 and 64 characters")
    private String newPassword;
    @NotBlank(message = "Confirmation password is required")
    private String confirmationPassword;
}