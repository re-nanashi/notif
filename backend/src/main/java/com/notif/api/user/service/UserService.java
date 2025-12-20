package com.notif.api.user.service;

import com.notif.api.common.util.Util;
import com.notif.api.user.dto.*;
import com.notif.api.user.entity.User;
import com.notif.api.user.exception.PasswordMismatchException;
import com.notif.api.common.exception.ResourceConflictException;
import com.notif.api.user.exception.UserAlreadyExistsException;
import com.notif.api.user.exception.UserNotFoundException;
import com.notif.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
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
            throw new UserAlreadyExistsException("User with email '" + request.getEmail() + "' already exists");
        }

        User newUser = User.builder()
                .firstName(request.getFirstName().strip()) // remove leading/trailing spaces
                .lastName(request.getLastName().strip())
                .email(request.getEmail()) // @Email annotation fails whitespaces
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);

        return this.convertUserToDto(newUser);
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
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        return this.convertUserToDto(existingUser);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        // Require at least one field to update
        if (Util.isNullOrBlank(firstName) && Util.isNullOrBlank(lastName)) {
            throw new IllegalArgumentException("No update values provided. At least one field must be changed.");
        }

        if (Util.hasValue(firstName)) existingUser.setFirstName(firstName);
        if (Util.hasValue(lastName)) existingUser.setLastName(lastName);

        userRepository.save(existingUser);

        return this.convertUserToDto(existingUser);
    }

    @Override
    public UserDTO changeEmail(ChangeEmailRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Validate current password
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        // Check if new email is the same as current
        String email = request.getNewEmail();
        if (existingUser.getEmail().equals(email)) {
            throw new IllegalStateException("Email is already set to this value");
        }
        // Check if email is already in use
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("Email '" + email + "' already in use");
        }

        existingUser.setEmail(request.getNewEmail());
        userRepository.save(existingUser);

        return convertUserToDto(existingUser);
    }

    @Override
    public UserDTO changePassword(ChangePasswordRequest request, UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Validate current password
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        // Check new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("The provided passwords must be identical");
        }

        existingUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(existingUser);

        return this.convertUserToDto(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        userRepository.deleteById(id);
    }

    // Internal utility: converts User entity to DTO
    private UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}