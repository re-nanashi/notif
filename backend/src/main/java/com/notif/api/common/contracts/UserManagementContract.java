package com.notif.api.common.contracts;

import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;

import java.util.Optional;

public interface UserManagementContract {
    UserDTO createUser(CreateUserRequest request);
    Optional<UserDTO> findByEmail(String email);
    boolean userAlreadyExists(String email);
    UserDTO enableUser(String token, String email);
}