package com.ticket_management_system.auth_service.auth_service.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Inject test values
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "dGhpc2lzdGVzdHNlY3JldEtleVRlc3RLZXlUZXN0S2V5dGhpc2lzdGVzdA=="); // base64 of "thisistestsecretkeytestkeytestkeythisistest"
        ReflectionTestUtils.setField(jwtUtils, "expiry", 3600);
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtils.generateJwtToken("testuser");
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtUtils.generateJwtToken("testuser");
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGetJwtTokenFromHeader() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer testtoken");

        String token = jwtUtils.getJwtTokenFromHeader(request);
        assertEquals("testtoken", token);
    }

    @Test
    void testInvalidToken() {
        assertFalse(jwtUtils.validateJwtToken("invalidtoken"));
    }
}
