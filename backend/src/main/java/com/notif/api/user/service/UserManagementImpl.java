package com.notif.api.user.service;

import com.notif.api.common.contracts.UserManagementContract;
import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;
import com.notif.api.user.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserManagementImpl implements UserManagementContract {
    private final UserService userService;
    private final VerificationTokenService tokenService;

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return Optional.ofNullable(userService.getUserByEmail(email));
    }

    @Override
    public boolean userAlreadyExists(String email) {
        return userService.userAlreadyExists(email);
    }

    @Override
    public UserDTO enableUser(String token, String email) {
        User savedUser = tokenService.validateVerificationToken(token, email);

        return userService.convertUserToDto(savedUser);
    }
}