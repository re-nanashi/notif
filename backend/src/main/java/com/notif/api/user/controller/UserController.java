package com.notif.api.user.controller;

import com.notif.api.common.exception.WrongPasswordException;
import com.notif.api.common.response.ApiResponse;
import com.notif.api.user.dto.ChangePasswordRequest;
import com.notif.api.user.dto.CreateUserRequest;
import com.notif.api.user.dto.UpdateUserRequest;
import com.notif.api.user.dto.UserDTO;
import com.notif.api.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private UserService userService;

    // admin-only
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        UserDTO user = userService.createUser(createUserRequest);
        // TODO (Authentication): JWT response; User registration is implemented on Auth
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User creation success", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Success", user));
    }

    // TODO (Authorization):
    //  [ ] Only connected users can update their own user password. Admin can update anyone's profile.
    //  [ ] We should check the userId on the endpoint and check if the connected user has the same ID (or it can be done
    //  on Authorization implementation.
    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest request, @PathVariable Long userId) throws WrongPasswordException {
        UserDTO user = userService.changePassword(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Password changed successfully", user));
    }

    // TODO: Only connected users can update their own user profile. Admin can update anyone's profile.
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
        UserDTO user = userService.updateUser(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Update success", user));
    }

    // Admin-only
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User deleted successfully.", null));
    }
}