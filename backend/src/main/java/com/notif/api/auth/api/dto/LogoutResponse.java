package com.notif.api.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response object returned after a successful logout operation.
 * Contains a message describing the result and a timestamp of when the logout occurred.
 */
@Data
@AllArgsConstructor
public class LogoutResponse {
    private String message;
    private LocalDateTime timestamp;
}