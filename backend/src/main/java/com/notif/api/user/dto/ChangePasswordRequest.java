package com.notif.api.user.dto;

import com.notif.api.common.validation.PasswordMatchable;
import com.notif.api.common.validation.PasswordMatches;
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
 * - {@code confirmPassword} must match {@code newPassword}
 */
@Data
@PasswordMatches
public class ChangePasswordRequest implements PasswordMatchable {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/]).{8,64}$",
            message = "Password must contain uppercase, lowercase, number, and a special character"
    )
    private String password;
    private String confirmPassword;
}