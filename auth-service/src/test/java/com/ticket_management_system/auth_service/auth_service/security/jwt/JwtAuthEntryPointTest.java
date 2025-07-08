package com.ticket_management_system.auth_service.auth_service.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket_management_system.auth_service.auth_service.security.jwt.JwtAuthEntryPoint;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class JwtAuthEntryPointTest {

    private JwtAuthEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthEntryPoint();
    }

    @Test
    void testCommence() throws Exception {
        JwtAuthEntryPoint entryPoint = new JwtAuthEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = new UsernameNotFoundException("Unauthorized");

        // Mock output stream
        ServletOutputStream outputStream = new DelegatingServletOutputStream(new ByteArrayOutputStream());
        when(response.getOutputStream()).thenReturn(outputStream);
        when(request.getServletPath()).thenReturn("/secure/api");

        // Act
        entryPoint.commence(request, response, authException);

        // Verify expected response status
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
