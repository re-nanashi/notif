package com.notif.api.auth.api.dto;

import com.notif.api.core.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is required.")
    @ValidEmail
    private String email;
    @NotBlank(message = "Password is required.")
    private String password;
}