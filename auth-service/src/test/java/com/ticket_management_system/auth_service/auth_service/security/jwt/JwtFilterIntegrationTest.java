package com.ticket_management_system.auth_service.auth_service.security.jwt;

import com.ticket_management_system.auth_service.auth_service.security.jwt.JwtUtils;
import com.ticket_management_system.auth_service.auth_service.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void whenUnauthorizedRequest_thenReturns401() throws Exception {
        mockMvc.perform(get("/protected/endpoint"))
                .andExpect(status().isUnauthorized());
    }
}
