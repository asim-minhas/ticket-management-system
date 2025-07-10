package com.ticket_management_system.auth_service.auth_service.security.response;

import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private String username;
    private UserRole role;
}
