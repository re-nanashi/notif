package com.notif.api.user.api.controller;

import com.notif.api.user.api.dto.*;
import com.notif.api.core.dto.ApiResponse;
import com.notif.api.user.application.service.UserService;
import com.notif.api.user.application.service.VerificationTokenService;
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
    private final VerificationTokenService tokenService;

    /**
     * Creates a new user (admin-only).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserResponse createdUser = userService.createUser(request);
        tokenService.generateVerificationToken(createdUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<UserResponse>("User created successfully.", createdUser));
    }

    /**
     * Retrieves a user by their UUID (admin-only).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<UserResponse>("Success", user));
    }

    /**
     * Retrieves all users (admin-only).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<List<UserResponse>>("Success", users));
    }

    /**
     * Updates a user's profile (user and admin).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @RequestBody UpdateUserProfileRequest request,
            @PathVariable UUID id
    ) {
        // TODO (Authorization):
        //  [ ] Only connected users can update their own data (profile, password, etc). Admin can update anyone's profile.
        //  [ ] We should check the ID on the endpoint and check if the connected user has the same ID.
        UserResponse updatedUser = userService.updateUserProfile(request, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<UserResponse>("User updated successfully.", updatedUser));
    }

    /**
     * Changes a user's email (user and admin).
     *
     * Business rules:
     * - TODO (Future): Requires validation of current email/password internally.
     * - TODO (Future): Marks email as unverified. User needs to verify email again.
     */
    @PatchMapping("/{id}/email")
    public ResponseEntity<ApiResponse<UserResponse>> changeEmail(
            @RequestBody @Valid ChangeEmailRequest request,
            @PathVariable UUID id
    ) {
        UserResponse updatedUser = userService.changeEmail(request, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<UserResponse>("Email changed successfully.", updatedUser));
    }

    /**
     * Changes a user's password (user and admin).
     *
     * Business rules:
     * - Requires current password for verification.
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            @PathVariable UUID id
    ) {
        userService.changePassword(request, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<Void>("Password changed successfully.", null));
    }

    /**
     * Deletes a user (admin-only).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<Void>("User deleted successfully.", null));
    }
}