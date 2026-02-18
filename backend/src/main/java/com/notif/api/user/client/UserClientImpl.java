package com.notif.api.user.client;

import com.notif.api.core.exception.*;
import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.application.service.UserService;
import com.notif.api.user.application.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserClientImpl implements UserClient {
    private final UserService userService;
    private final VerificationTokenService tokenService;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return userService.convertUserToResponse(user);
        } catch (AlreadyExistsException ex) {
            throw new UserClientException(ex.getMessage(), ex.getErrorCode());
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.", ErrorCodes.INTERNAL_ERROR);
        }
    }

    @Override
    public UserResponse getByUserEmail(String email) {
        try {
            User user = userService.getUserByEmail(email);
            return userService.convertUserToResponse(user);
        } catch (NotFoundException ex) {
            throw new UserClientException(ex.getMessage(), ex.getErrorCode());
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.", ErrorCodes.INTERNAL_ERROR);
        }
    }

    @Override
    public UserResponse enableUser(String token, String email) {
        try {
            tokenService.validateVerificationToken(token, email);
            User user = userService.enableUser(email);

            return userService.convertUserToResponse(user);
        } catch (NotFoundException | InvalidTokenException | TokenExpiredException ex) {
            throw new UserClientException(ex.getMessage(), ex.getErrorCode());
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.",ErrorCodes.INTERNAL_ERROR);
        }
    }
}