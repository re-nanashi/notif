package com.notif.api.data;

import com.notif.api.user.entity.Role;
import com.notif.api.user.entity.User;
import com.notif.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// TODO: Move to user package
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String DEFAULT_ADMIN_EMAIL;
    @Value("${admin.password}")
    private String DEFAULT_ADMIN_PASSWORD;

    @Value("${manager.email}")
    private String DEFAULT_MANAGER_EMAIL;
    @Value("${manager.password}")
    private String DEFAULT_MANAGER_PASSWORD;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        // Initializes data only users are missing
        initializeAdministrator();
        initializeDefaultManager();
    }

    private void initializeDefaultManager() {
        if (userRepository.existsByEmail(DEFAULT_MANAGER_EMAIL)) {
            return;
        }

        User manager = User.builder()
                .email(DEFAULT_MANAGER_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_MANAGER_PASSWORD))
                .firstName("Micah")
                .lastName("Manager")
                .role(Role.MANAGER)
                .enabled(true)
                .build();

        userRepository.save(manager);

        System.out.println("MANAGER was created.");
    }

    private void initializeAdministrator() {
        if (userRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
            return;
        }

        User admin = User.builder()
                .email(DEFAULT_ADMIN_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                .firstName("John")
                .lastName("Admin")
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);

        System.out.println("ADMIN was created.");
    }
}