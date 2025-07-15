package com.ticket_management_system.auth_service.auth_service.service;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;
import com.ticket_management_system.auth_service.auth_service.services.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);

    @Test
    void findUserByEmail_found_returnsUser() {
        User user = new User("id", "John Doe", "john@example.com", "password", "",UserRole.ROLE_ADMIN, null, null, true, true, true, true);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        var result = userService.findUserByEmail("john@example.com");

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void findUserByEmail_notFound_throwsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userService.findUserByEmail("notfound@example.com"));
    }
}
