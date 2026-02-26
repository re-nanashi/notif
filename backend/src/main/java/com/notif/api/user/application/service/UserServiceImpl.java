package com.notif.api.user.application.service;

import com.notif.api.core.domain.event.EventPublisher;
import com.notif.api.core.exception.ErrorCode;
import com.notif.api.core.exception.NotFoundException;
import com.notif.api.core.exception.ValidationException;
import com.notif.api.user.api.dto.*;
import com.notif.api.user.application.dto.CreateUserRequest;
import com.notif.api.user.domain.event.UserDeletedEvent;
import com.notif.api.user.domain.model.Role;
import com.notif.api.user.domain.model.User;
import com.notif.api.user.domain.exception.InvalidPasswordException;
import com.notif.api.user.domain.repository.UserRepository;
import com.notif.api.core.utils.Util;
import com.notif.api.core.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for user management operations.
 *
 * Handles user lifecycle operations including creation, updates, authentication data mapping, and account
 * state management.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    /**
     * Creates a new user with default USER role and disabled verification status.
     * Also generates a verification token after user persistence.
     */
    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "User with email '" + request.getEmail() + "' already exists.",
                    ErrorCode.USER_ALREADY_EXISTS
            );
        }

        User newUser = User.builder()
                .firstName(request.getFirstName().strip())
                .lastName(request.getLastName().strip())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        User savedUser = userRepository.save(newUser);

        // Trigger verification workflow
        verificationTokenService.generateVerificationToken(savedUser);

        return savedUser;
    }

    /**
     * Retrieves user by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));
    }

    /**
     * Retrieves user by email address.
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User with email '" + email + "' not found.",
                        ErrorCode.USER_NOT_FOUND
                ));
    }

    /**
     * Retrieves all users.
     * TODO:
     *  For large datasets, use pagination and sorting with methods like findAll(Pageable pageable)
     *  or findAll(Sort sort) to fetch data in manageable chunks.
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates user profile information.
     */
    @Override
    @Transactional
    public User updateUser(UpdateUserRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        // Require at least one field to be updated
        if (Util.isNullOrBlank(firstName) && Util.isNullOrBlank(lastName)) {
            throw new ValidationException("At least one field must be provided.", ErrorCode.MISSING_REQUIRED_FIELD);
        }

        if (!Util.isNullOrBlank(firstName)) existingUser.setFirstName(firstName);
        if (!Util.isNullOrBlank(lastName)) existingUser.setLastName(lastName);

        return userRepository.save(existingUser);
    }

    /**
     * Enables user account after verification.
     */
    @Override
    @Transactional
    public User enableUser(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + email + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        existingUser.setEnabled(true);

        return userRepository.save(existingUser);
    }

    /**
     * Changes user email after password verification.
     */
    @Override
    @Transactional
    public User changeEmail(ChangeEmailRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        // Verify current password before allowing sensitive changes
        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException("The password provided is incorrect.", ErrorCode.USER_INVALID_CREDENTIALS);
        }
        // Check if email is already in use
        String email = request.getNewEmail();
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    "Email '" + email + "' already in use.",
                    ErrorCode.USER_EMAIL_ALREADY_EXISTS
            );
        }

        existingUser.setEmail(request.getNewEmail());

        return userRepository.save(existingUser);

    }

    /**
     * Changes user password after validating current password.
     */
    @Override
    @Transactional
    public User changePassword(ChangePasswordRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException("The password provided is incorrect.", ErrorCode.USER_INVALID_CREDENTIALS);
        }

        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(existingUser);
    }

    /**
     * Deletes user account and publishes domain deletion event.
     */
    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        eventPublisher.publish(new UserDeletedEvent(id));

        userRepository.deleteById(id);
    }

    /**
     * Maps domain User entity to API response DTO.
     */
    @Override
    public UserResponse convertUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .emailVerified(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Maps domain User entity to authentication security DTO.
     */
    @Override
    public UserAuthDetails convertUserToAuthDetails(User user) {
        return UserAuthDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .build();
    }
}