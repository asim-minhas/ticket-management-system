package com.ticket_management_system.auth_service.auth_service.security.service;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void createUser_returnsCorrectUserDetailsImpl() {
        User user = new User("id", "John", "john@example.com", "password","", UserRole.ADMIN, null, null, true, true, true, true);

        UserDetailsImpl userDetails = UserDetailsImpl.createUser(user);

        assertNotNull(userDetails);
        assertEquals("john@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
