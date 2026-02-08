package com.notif.api.user.service;

import com.notif.api.common.constants.ErrorCodes;
import com.notif.api.common.exception.ResourceNotFoundException;
import com.notif.api.common.exception.ValidationException;
import com.notif.api.user.dto.*;
import com.notif.api.user.entity.User;
import com.notif.api.user.exception.InvalidPasswordException;
import com.notif.api.user.exception.PasswordMismatchException;
import com.notif.api.user.repository.UserRepository;
import com.notif.api.common.util.Util;
import com.notif.api.common.exception.ResourceConflictException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException(
                    "User with email '" + request.getEmail() + "' already exists",
                    ErrorCodes.USER_ALREADY_EXISTS
            );
        }

        User newUser = User.builder()
                .firstName(request.getFirstName().strip()) // remove leading/trailing spaces
                .lastName(request.getLastName().strip())
                .email(request.getEmail()) // @Email annotation fails whitespaces
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);

        return convertUserToDto(newUser);
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
                        "User with ID " + id + " not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email '" + email + "' not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        // Require at least one field to update
        if (Util.isNullOrBlank(firstName) && Util.isNullOrBlank(lastName)) {
            throw new ValidationException(
                    "At least one field must be provided for update",
                    ErrorCodes.NO_FIELDS_TO_UPDATE
            );
        }

        if (!Util.isNullOrBlank(firstName)) existingUser.setFirstName(firstName);
        if (!Util.isNullOrBlank(lastName)) existingUser.setLastName(lastName);

        userRepository.save(existingUser);

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO changeEmail(ChangeEmailRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        // Check if password is incorrect
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException(
                    "The password provided is incorrect",
                    ErrorCodes.INVALID_CREDENTIALS
            );
        }
        // Check if email is already in use
        String email = request.getNewEmail();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException(
                    "Email '" + email + "' already in use",
                    ErrorCodes.EMAIL_ALREADY_EXISTS
            );
        }

        existingUser.setEmail(request.getNewEmail());

        userRepository.save(existingUser);

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO changePassword(ChangePasswordRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        // Check if password is incorrect
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException(
                    "The password provided is incorrect",
                    ErrorCodes.INVALID_CREDENTIALS
            );
        }
        // Check if new password does not match confirmation password
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException(
                    "The provided passwords must be identical",
                    ErrorCodes.PASSWORD_MISMATCH
            );
        }

        existingUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        userRepository.save(existingUser);

        return convertUserToDto(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        ErrorCodes.USER_NOT_FOUND
                ));

        userRepository.deleteById(id);
    }

    // Internal utility: converts User entity to DTO
    private UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}