package com.notif.api.user.service;

import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;
import com.notif.api.user.api.dto.ChangeEmailRequest;
import com.notif.api.user.api.dto.ChangePasswordRequest;
import com.notif.api.user.api.dto.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing users.
 *
 * Provides methods for creating, updating, retrieving, and deleting users, as well as changing email and password.
 */
public interface IUserService {
    /**
     * Creates a new user.
     *
     * @param request the user creation request containing email, password, and profile information
     * @return the created {@link UserDTO}
     */
    UserDTO createUser(CreateUserRequest request);

    /**
     * Retrieves all users.
     *
     * @return list of {@link UserDTO} representing all users
     */
    List<UserDTO> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id the UUID of the user
     * @return the {@link UserDTO} of the user
     */
    UserDTO getUserById(UUID id);

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the {@link UserDTO} of the user
     */
    UserDTO getUserByEmail(String email);

    /**
     * Updates a user's profile information.
     *
     * @param request the fields to update
     * @param id      the UUID of the user
     * @return the updated {@link UserDTO}
     */
    UserDTO updateUser(UpdateUserRequest request, UUID id);

    /**
     * Changes a user's email address.
     *
     * @param request the new email and current password for verification
     * @param id      the UUID of the user
     * @return the updated {@link UserDTO}
     */
    UserDTO changeEmail(ChangeEmailRequest request, UUID id);

    /**
     * Changes a user's password.
     *
     * @param request contains current password, new password, and confirmation
     * @param id      the UUID of the user
     * @return the updated {@link UserDTO}
     */
    UserDTO changePassword(ChangePasswordRequest request, UUID id);

    /**
     * Deletes a user by their ID.
     *
     * @param id the UUID of the user to delete
     */
    void deleteUser(UUID id);
}