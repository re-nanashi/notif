package com.notif.api.auth.dto;

import com.notif.api.common.validation.PasswordMatchable;
import com.notif.api.common.validation.PasswordMatches;
import com.notif.api.common.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO used for registering a new user account.
 *
 * Contains all required fields for registration, including account credentials
 * and basic user profile information.
 *
 * Validation rules:
 * - {@code email} must not be blank and must be a valid email format
 * - {@code password} must not be blank, between 8-64 characters, and contain
 *   at least one uppercase letter, one lowercase letter, and one number
 * - {@code confirmPassword} must match {@code password}
 * - {@code firstName} and {@code lastName} must not be blank
 */
@Data
@PasswordMatches
public class RegisterRequest implements PasswordMatchable {
    @NotBlank(message = "Email is required.")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/]).{8,64}$",
            message = "Password must contain uppercase, lowercase, number, and a special character."
    )
    private String password;
    private String confirmPassword;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;
}