package com.notif.api.user.client;

import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.api.dto.UserDTO;

import java.util.Optional;

public interface UserManagementContract {
    UserDTO createUser(CreateUserRequest request);
    Optional<UserDTO> findByEmail(String email);
    boolean userAlreadyExists(String email);
    UserDTO enableUser(String token, String email);
}