package com.notif.api.user.infrastructure.client;

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
        } catch (BusinessException ex) {
            // propagate domain exception
            throw ex;
        } catch (Exception ex) {
            // wrap unexpected exceptions for inter-module consistency.
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    @Override
    public UserResponse getByUserEmail(String email) {
        try {
            User user = userService.getUserByEmail(email);
            return userService.convertUserToResponse(user);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    @Override
    public UserResponse enableUser(String token, String email) {
        try {
            tokenService.validateVerificationToken(token, email);
            User user = userService.enableUser(email);

            return userService.convertUserToResponse(user);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    @Override
    public UserResponse requestVerification(String email) {
        try {
            User user = userService.getUserByEmail(email);

            // Check if user is already verified
            if (user.isEnabled()) {
                throw new ConflictException(
                        "User is already verified.",
                        ErrorCode.USER_ALREADY_VERIFIED
                );
            }

            // Void all of user's pending verification tokens
            tokenService.voidExistingTokens(email);

            // Generate new verification token
            tokenService.generateVerificationToken(email);

            return userService.convertUserToResponse(user);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }
}