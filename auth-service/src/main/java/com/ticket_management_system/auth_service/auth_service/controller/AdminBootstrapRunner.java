package com.ticket_management_system.auth_service.auth_service.controller;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("bootstrap")
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapRunner implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "platform-admin@yourcorp.io";
        if (userRepo.existsByEmail(email)) return;

        User admin = User.builder()
                .email(email)
                .password(passwordEncoder.encode("StartupP@ss!"))
                .name("Platform Admin")
                .companyName("YOUR-CORP")
                .role(UserRole.ROLE_ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        userRepo.save(admin);
        log.warn("GLOBAL_ADMIN bootstrap account created: {}", email);
    }
}
