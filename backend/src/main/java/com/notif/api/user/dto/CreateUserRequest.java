package com.notif.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO used for creating a new user account (admin-only).
 *
 * Contains all required fields for registration, including account credentials
 * and basic user profile information.
 *
 * Validation rules:
 * - {@code email} must not be blank and must be a valid email format
 * - {@code password} must not be blank, between 8-64 characters, and contain
 *   at least one uppercase letter, one lowercase letter, and one number
 * - {@code firstName} and {@code lastName} must not be blank
 */
@Data
public class CreateUserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    // TODO (Authentication): Will consider OAuth registration flow therefore password may not always be required.
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?/]).{8,64}$",
            message = "Password must contain uppercase, lowercase, number, and a special character"
    )
    private String password;
    private String matchingPassword;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}