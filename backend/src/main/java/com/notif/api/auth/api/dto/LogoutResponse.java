package com.notif.api.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogoutResponse {
    private String message;
    private LocalDateTime timestamp;
}