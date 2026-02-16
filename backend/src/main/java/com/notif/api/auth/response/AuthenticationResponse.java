package com.notif.api.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// TODO (auth): refresh tokens
public class AuthenticationResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn; // in seconds
}