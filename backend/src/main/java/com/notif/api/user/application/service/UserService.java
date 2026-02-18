package com.notif.api.user.application.service;

import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.api.dto.ChangeEmailRequest;
import com.notif.api.user.api.dto.ChangePasswordRequest;
import com.notif.api.user.api.dto.UpdateUserRequest;
import com.notif.api.user.domain.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing users.
 *
 * Provides methods for creating, updating, retrieving, and deleting users, as well as modifying user information.
 */
public interface UserService {
    User createUser(CreateUserRequest request);
    User getUserById(UUID id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(UpdateUserRequest request, UUID id);
    User enableUser(String email);
    User changeEmail(ChangeEmailRequest request, UUID id);
    User changePassword(ChangePasswordRequest request, UUID id);
    void deleteUser(UUID id);
    UserResponse convertUserToResponse(User user);
}