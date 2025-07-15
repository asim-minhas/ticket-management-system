package com.ticket_management_system.auth_service.auth_service.security.service;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    void loadUserByUsername_userFound_returnsUserDetails() {
        User user = new User("id", "John Doe", "john@example.com", "password","" ,UserRole.ROLE_ADMIN, null, null, true, true, true, true);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("john@example.com");

        assertNotNull(result);
        assertEquals("john@example.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));
    }
}
