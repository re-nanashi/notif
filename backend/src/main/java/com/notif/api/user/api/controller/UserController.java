package com.notif.api.user.api.controller;

import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.core.dto.ApiResponse;
import com.notif.api.user.api.dto.ChangeEmailRequest;
import com.notif.api.user.api.dto.ChangePasswordRequest;
import com.notif.api.user.api.dto.UpdateUserRequest;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.service.UserService;
import com.notif.api.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing users.
 *
 * Provides endpoints for user creation, retrieval, updates, email/password changes, and deletion.
 *
 * Business rules / notes:
 * - Admin-only endpoints: create, get all users, delete.
 * - User can update own profile, email, and password. User can also delete own account.
 */
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Creates a new user (admin-only).
     *
     * @param request the user creation payload
     * @return {@link ApiResponse} containing the created {@link UserResponse}
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.createUser(request);
        UserResponse response = userService.convertUserToResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User created successfully.", response));
    }

    /**
     * Retrieves all users (admin-only).
     *
     * @return {@link ApiResponse} containing a list of {@link UserResponse}
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(userService::convertUserToResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", users));
    }

    /**
     * Retrieves a user by their UUID (admin-only).
     *
     * @param id the UUID of the user
     * @return {@link ApiResponse} containing the {@link UserResponse}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        UserResponse response = userService.convertUserToResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", response));
    }

    /**
     * Updates a user's profile (user and admin).
     *
     * @param request the update payload
     * @param id      the UUID of the user to update
     * @return {@link ApiResponse} containing the updated {@link UserResponse}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable UUID id) {
        // TODO (Authorization):
        //  [ ] Only connected users can update their own data (profile, password, etc). Admin can update anyone's profile.
        //  [ ] We should check the ID on the endpoint and check if the connected user has the same ID.
        User user = userService.updateUser(request, id);
        UserResponse response = userService.convertUserToResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User updated successfully.", response));
    }

    /**
     * Changes a user's email (user and admin).
     *
     * Business rules:
     * - TODO (Future): Requires validation of current email/password internally.
     * - TODO (Future): Marks email as unverified. User needs to verify email again.
     *
     * @param request the {@link ChangeEmailRequest} containing the new email
     * @param id      the UUID of the user
     * @return {@link ApiResponse} containing the updated {@link UserResponse}
     */
    @PatchMapping("/{id}/email")
    public ResponseEntity<ApiResponse> changeEmail(@RequestBody @Valid ChangeEmailRequest request, @PathVariable UUID id) {
        User user = userService.changeEmail(request, id);
        UserResponse response = userService.convertUserToResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Email changed successfully.", response));
    }

    /**
     * Changes a user's password (user and admin).
     *
     * Business rules:
     * - Requires current password for verification.
     *
     * @param request the {@link ChangePasswordRequest} containing the current and new passwords
     * @param id      the UUID of the user
     * @return {@link ApiResponse} containing the updated {@link UserResponse}
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request, @PathVariable UUID id) {
        User user = userService.changePassword(request, id);
        UserResponse response = userService.convertUserToResponse(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Password changed successfully.", response));
    }

    /**
     * Deletes a user (admin-only).
     *
     * @param id the UUID of the user to delete
     * @return {@link ApiResponse} confirming deletion
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User deleted successfully.", null));
    }
}