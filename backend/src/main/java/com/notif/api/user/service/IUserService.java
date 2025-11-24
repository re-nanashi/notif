package com.notif.api.user.service;

import com.notif.api.user.dto.UserDTO;
import com.notif.api.user.dto.CreateUserRequest;
import com.notif.api.user.dto.UpdateUserRequest;
import com.notif.api.user.dto.ChangePasswordRequest;

import java.util.List;

public interface IUserService {
    UserDTO createUser(CreateUserRequest user);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long userId);
    UserDTO updateUser(UpdateUserRequest request, Long userId);
    UserDTO changePassword(ChangePasswordRequest request, Long userId);
    void deleteUser(Long userId);
}