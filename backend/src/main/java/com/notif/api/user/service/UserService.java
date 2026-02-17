package com.notif.api.user.service;

import com.notif.api.core.exception.ErrorCodes;
import com.notif.api.core.exception.ResourceNotFoundException;
import com.notif.api.core.exception.ValidationException;
import com.notif.api.common.request.CreateUserRequest;
import com.notif.api.common.response.UserDTO;
import com.notif.api.user.entity.Role;
import com.notif.api.user.entity.User;
import com.notif.api.user.exception.InvalidPasswordException;
import com.notif.api.user.repository.UserRepository;
import com.notif.api.core.utils.Util;
import com.notif.api.core.exception.ResourceConflictException;
import com.notif.api.user.api.dto.ChangeEmailRequest;
import com.notif.api.user.api.dto.ChangePasswordRequest;
import com.notif.api.user.api.dto.UpdateUserRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(
                    "User with email '" + request.getEmail() + "' already exists.",
                    ErrorCodes.USER_ALREADY_EXISTS
            );
        }

        User newUser = User.builder()
                .firstName(request.getFirstName().strip()) // remove leading/trailing spaces
                .lastName(request.getLastName().strip())
                .email(request.getEmail()) // @Email annotation fails whitespaces
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();

        User savedUser = userRepository.save(newUser);

        return convertUserToDto(savedUser);
    }

    // TODO (Future):
    //  For large datasets, use pagination and sorting with methods like findAll(Pageable pageable)
    //  or findAll(Sort sort) to fetch data in manageable chunks.
    @Override
    public List<UserDTO> getAllUsers() {
        // Convert all users to DTOs
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email '" + email + "' not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        return convertUserToDto(existingUser);
    }

    public boolean userAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        // Require at least one field to update
        if (Util.isNullOrBlank(firstName) && Util.isNullOrBlank(lastName)) {
            throw new ValidationException(
                    "At least one field must be provided.",
                    ErrorCodes.NO_FIELDS_TO_UPDATE
            );
        }

        if (!Util.isNullOrBlank(firstName)) existingUser.setFirstName(firstName);
        if (!Util.isNullOrBlank(lastName)) existingUser.setLastName(lastName);

        User savedUser = userRepository.save(existingUser);

        return convertUserToDto(savedUser);
    }

    @Override
    public UserDTO changeEmail(ChangeEmailRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        // Check if password is incorrect
        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException(
                    "The password provided is incorrect.",
                    ErrorCodes.INVALID_CREDENTIALS
            );
        }
        // Check if email is already in use
        String email = request.getNewEmail();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException(
                    "Email '" + email + "' already in use.",
                    ErrorCodes.EMAIL_ALREADY_EXISTS
            );
        }

        existingUser.setEmail(request.getNewEmail());

        User savedUser = userRepository.save(existingUser);

        return convertUserToDto(savedUser);
    }

    @Override
    public UserDTO changePassword(ChangePasswordRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        // Check if password is incorrect
        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException(
                    "The password provided is incorrect.",
                    ErrorCodes.INVALID_CREDENTIALS
            );
        }

        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(existingUser);

        return convertUserToDto(savedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found.",
                        ErrorCodes.USER_NOT_FOUND
                ));

        userRepository.deleteById(id);
    }

    // Converts User entity to DTO
    public UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}