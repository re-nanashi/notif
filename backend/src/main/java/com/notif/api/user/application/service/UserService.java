package com.notif.api.user.application.service;

import com.notif.api.user.api.dto.*;
import com.notif.api.user.api.dto.CreateUserRequest;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing users.
 *
 * Provides methods for creating, updating, retrieving, and deleting users, as well as modifying user information.
 */
public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    UserAuthDetails getUserAuthDetailsById(UUID id);
    UserAuthDetails getUserAuthDetailsByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse enableUser(String email);
    UserResponse updateUserProfile(UpdateUserProfileRequest request, UUID id);
    UserResponse changeEmail(ChangeEmailRequest request, UUID id);
    void changePassword(ChangePasswordRequest request, UUID id);
    void deleteUser(UUID id);
}