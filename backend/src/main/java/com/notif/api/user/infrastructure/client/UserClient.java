package com.notif.api.user.infrastructure.client;

import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;

import java.util.UUID;

public interface UserClient {
    UserResponse createUser(CreateUserRequest request) throws UserClientException;
    UserResponse getUserById(UUID id) throws UserClientException;
    UserResponse getByUserEmail(String email) throws UserClientException;
    UserResponse enableUser(String token, String email) throws UserClientException;
    UserResponse requestVerification(String email) throws UserClientException;
}