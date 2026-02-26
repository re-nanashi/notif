package com.notif.api.user.application.service;

import com.notif.api.user.api.dto.*;
import com.notif.api.user.application.dto.CreateUserRequest;
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
    User enableUser(String email);
    User updateUser(UpdateUserRequest request, UUID id);
    User changeEmail(ChangeEmailRequest request, UUID id);
    User changePassword(ChangePasswordRequest request, UUID id);
    void deleteUser(UUID id);
    UserResponse convertUserToResponse(User user);
    UserAuthDetails convertUserToAuthDetails(User user);
}