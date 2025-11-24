package com.notif.api.user.service;

import com.notif.api.common.exception.AlreadyExistsException;
import com.notif.api.common.exception.WrongPasswordException;
import com.notif.api.user.dto.ChangePasswordRequest;
import com.notif.api.user.dto.CreateUserRequest;
import com.notif.api.user.dto.UpdateUserRequest;
import com.notif.api.user.dto.UserDTO;
import com.notif.api.user.entity.User;
import com.notif.api.user.exceptions.PasswordMismatchException;
import com.notif.api.user.exceptions.UserAlreadyExistsException;
import com.notif.api.user.exceptions.UserNotFoundException;
import com.notif.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // Check if email already exists in the users database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email already exists: " + request.getEmail());
        }

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);

        return this.convertUserToDto(newUser);
    }

    // TODO:
    //  For large datasets, use pagination and sorting with methods like findAll(Pageable pageable)
    //  or findAll(Sort sort) to fetch data in manageable chunks.
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToDto).toList();
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return this.convertUserToDto(existingUser);
    }

    // TODO: Should update the email
    @Override
    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        // Check if the modification request is valid, meaning it is not what is currently saved
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());

        // Check if email already exists in the users database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists.");
        }
        existingUser.setEmail(request.getEmail()); //

        userRepository.save(existingUser);

        return this.convertUserToDto(existingUser);
    }

    @Override
    public UserDTO changePassword(ChangePasswordRequest request, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        // Check if the current password is incorrect
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }
        // Check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordMismatchException("Passwords are not the same");
        }

        // Update the password
        existingUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        userRepository.save(existingUser);

        return this.convertUserToDto(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        userRepository.deleteById(userId);
    }

    private UserDTO convertUserToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}