package com.notif.api.auth.application.dto;

import com.notif.api.auth.api.dto.LoginResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResult {
    private LoginResponse response;
    private String refreshToken;
}