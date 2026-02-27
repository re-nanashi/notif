package com.notif.api.user.api.client;

import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.api.dto.CreateUserRequest;

import java.util.UUID;

/**
 * Client abstraction for communicating with the User service.
 *
 * Provides user-related operations such as registration, retrieval, authentication detail lookup, and
 * account verification.
 *
 * Implementations are responsible for handling remote communication and translating errors into UserClientException.
 */
public interface UserClient {
    UserResponse createUser(CreateUserRequest request) throws UserClientException;
    UserResponse getUserById(UUID id) throws UserClientException;
    UserResponse getUserByEmail(String email) throws UserClientException;
    UserAuthDetails getUserAuthDetailsByEmail(String email) throws UserClientException;
    UserResponse enableUser(String verificationToken, String email) throws UserClientException;
    UserResponse requestVerification(String email) throws UserClientException;
}