package com.notif.api.user.service;

import com.notif.api.common.response.UserDTO;
import com.notif.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Spy
    private ModelMapper modelMapper;

    @BeforeAll
    static void setup() {
        log.info(() -> "[TEST] UserService");
    }

    @Test
    @DisplayName("Returns an empty list of users")
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("Returns all users when users exists")
    void shouldReturnAllUsersWhenUsersExists() {}

    @Disabled
    @Test
    @DisplayName("Returns user DTO when user ID exists")
    void shouldReturnUserWhenIdExists() {}

    @Disabled
    @Test
    @DisplayName("Throws a UserNotFound exception when ID does not exist")
    void shouldThrowUserExceptionWhenUserNotFound() {}
}