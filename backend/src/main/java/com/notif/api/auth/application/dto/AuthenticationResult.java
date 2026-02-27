package com.notif.api.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic wrapper representing the result of an authentication operation.
 * Contains the primary response payload along with an issued refresh token.
 *
 * @param <T> the type of the authentication response body (LoginResponse/LogoutResponse)
 */
@Data
@AllArgsConstructor
public class AuthenticationResult<T> {
    private T response;
    private String refreshToken;
}