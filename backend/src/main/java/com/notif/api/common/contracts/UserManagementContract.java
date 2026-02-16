package com.notif.api.common.contracts;

import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;

public interface UserManagementContract {
    UserDTO createUser(CreateUserRequest request);
    boolean existsByEmail(String email);
}
