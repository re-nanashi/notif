package com.notif.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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