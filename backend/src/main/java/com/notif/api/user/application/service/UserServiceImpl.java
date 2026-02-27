package com.notif.api.user.application.service;

import com.notif.api.core.domain.event.EventPublisher;
import com.notif.api.core.exception.ErrorCode;
import com.notif.api.core.exception.NotFoundException;
import com.notif.api.core.exception.ValidationException;
import com.notif.api.user.api.dto.*;
import com.notif.api.user.api.dto.CreateUserRequest;
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
    public UserResponse createUser(CreateUserRequest request) {
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

        return convertUserToResponse(savedUser);
    }

    /**
     * Retrieves user by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        return convertUserToResponse(user);
    }

    /**
     * Retrieves user by email address.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User with email '" + email + "' not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        return convertUserToResponse(user);
    }

    /**
     * Retrieves user authentication and authorization details by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthDetails getUserAuthDetailsById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        return convertUserToAuthDetails(user);
    }

    /**
     * Retrieves user authentication and authorization details by email.
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthDetails getUserAuthDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User with email '" + email + "' not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        return convertUserToAuthDetails(user);
    }

    /**
     * Retrieves all users.
     * TODO:
     *  For large datasets, use pagination and sorting with methods like findAll(Pageable pageable)
     *  or findAll(Sort sort) to fetch data in manageable chunks.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToResponse)
                .toList();
    }

    /**
     * Enables user account after verification.
     */
    @Override
    @Transactional
    public UserResponse enableUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "User with email " + email + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        if (user.isEnabled()) {
            throw new ConflictException("User is already verified.", ErrorCode.USER_ALREADY_VERIFIED);
        }

        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return convertUserToResponse(savedUser);
    }

    /**
     * Updates user profile information.
     */
    @Override
    @Transactional
    public UserResponse updateUserProfile(UpdateUserProfileRequest request, UUID id) {
        User user = userRepository.findById(id)
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

        if (!Util.isNullOrBlank(firstName)) user.setFirstName(firstName.strip());
        if (!Util.isNullOrBlank(lastName)) user.setLastName(lastName.strip());

        User savedUser = userRepository.save(user);

        return convertUserToResponse(savedUser);
    }

    /**
     * Changes user email after password verification.
     */
    @Override
    @Transactional
    public UserResponse changeEmail(ChangeEmailRequest request, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        // Verify current password before allowing sensitive changes
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
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

        user.setEmail(request.getNewEmail());

        User savedUser = userRepository.save(user);

        return convertUserToResponse(savedUser);
    }

    /**
     * Changes user password after validating current password.
     */
    @Override
    @Transactional
    public UserResponse changePassword(ChangePasswordRequest request, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCode.USER_NOT_FOUND
                ));

        // Verify current password before allowing sensitive changes
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("The password provided is incorrect.", ErrorCode.USER_INVALID_CREDENTIALS);
        }
        // Verify if new password is different from current password
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationException("New password must differ from current password.", ErrorCode.USER_SAME_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return convertUserToResponse(savedUser);
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

        userRepository.deleteById(id);

        eventPublisher.publish(new UserDeletedEvent(id));
    }

    /**
     * Maps domain User entity to API response DTO.
     */
    private UserResponse convertUserToResponse(User user) {
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
    private UserAuthDetails convertUserToAuthDetails(User user) {
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