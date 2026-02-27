package com.notif.api.user.api.client;

import com.notif.api.core.exception.*;
import com.notif.api.user.api.dto.UserAuthDetails;
import com.notif.api.user.api.dto.CreateUserRequest;
import com.notif.api.user.api.dto.UserResponse;
import com.notif.api.user.application.service.UserService;
import com.notif.api.user.application.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application-layer adapter that exposes user-related operations to other modules while enforcing a
 * consistent exception boundary.
 */
@Service
@AllArgsConstructor
public class UserClientImpl implements UserClient {
    private final UserService userService;
    private final VerificationTokenService tokenService;

    /**
     * Creates a new user and returns its public representation. Used for Auth/Registration flow.
     */
    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        try {
            UserResponse createdUser = userService.createUser(request);
            tokenService.generateVerificationToken(createdUser.getId());

            return createdUser;
        } catch (BusinessException ex) {
            // Preserve domain-level validation and rule violations, to be caught by global exception handler
            throw ex;
        } catch (Exception ex) {
            // Prevent leaking internal exceptions across module boundary
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Retrieves a user by unique identifier.
     */
    @Override
    public UserResponse getUserById(UUID id) {
        try {
            return userService.getUserById(id);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Retrieves a user by email address.
     */
    @Override
    public UserResponse getUserByEmail(String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Returns authentication-specific details required by security layer (used for login, JWT generation, and
     * Spring Security integration).
     */
    @Override
    public UserAuthDetails getUserAuthDetailsById(UUID id) throws UserClientException {
        try {
            // Used by authentication layer (e.g., JWT / Spring Security)
            return userService.getUserAuthDetailsById(id);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Returns authentication-specific details required by security layer (used for login, JWT generation, and
     * Spring Security integration).
     */
    @Override
    public UserAuthDetails getUserAuthDetailsByEmail(String email) {
        try {
            // Used by authentication layer (e.g., JWT / Spring Security)
            return userService.getUserAuthDetailsByEmail(email);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Enables (verifies) a user account after validating the verification token.
     * Used only during email verification flow.
     */
    @Override
    @Transactional
    public UserResponse enableUser(String token, String email) {
        try {
            UserResponse user = userService.getUserByEmail(email);

            // Validate token ownership and state before enabling account
            tokenService.validateVerificationToken(token, user.getId());

            return userService.enableUser(user.getEmail());
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }

    /**
     * Triggers a new verification process (e.g., resend email verification token).
     */
    @Override
    @Transactional
    public UserResponse requestVerification(String email) {
        try {
            UserResponse user = userService.getUserByEmail(email);

            // Prevent issuing verification tokens for already verified accounts
            if (user.isEmailVerified()) {
                throw new ConflictException(
                        "User is already verified.",
                        ErrorCode.USER_ALREADY_VERIFIED
                );
            }

            // Invalidate any existing verification tokens before issuing a new one
            tokenService.voidPendingTokensByUserId(user.getId());
            // Issue new verification token
            tokenService.generateVerificationToken(user.getId());

            return user;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserClientException("Unexpected error in user service.");
        }
    }
}