package com.notif.api.auth.application.dto;

import com.notif.api.auth.api.dto.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResult {
    private LoginResponse response;
    private String refreshToken;
}