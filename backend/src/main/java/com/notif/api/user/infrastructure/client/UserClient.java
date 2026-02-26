package com.notif.api.user.infrastructure.client;

import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;

import java.util.UUID;

public interface UserClient {
    UserResponse createUser(CreateUserRequest request) throws UserClientException;
    UserResponse getUserById(UUID id) throws UserClientException;
    UserResponse getUserByEmail(String email) throws UserClientException;
    UserAuthDetails getUserAuthDetailsByEmail(String email) throws UserClientException;
    UserResponse enableUser(String verificationToken, String email) throws UserClientException;
    UserResponse requestVerification(String email) throws UserClientException;
}