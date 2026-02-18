package com.notif.api.user.client;

import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;

public interface UserClient {
    UserResponse createUser(CreateUserRequest request) throws UserClientException;
    UserResponse getByUserEmail(String email) throws UserClientException;
    UserResponse enableUser(String token, String email) throws UserClientException;
}