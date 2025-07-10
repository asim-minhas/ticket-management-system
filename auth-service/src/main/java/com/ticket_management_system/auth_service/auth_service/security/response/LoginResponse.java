package com.ticket_management_system.auth_service.auth_service.security.response;

import com.ticket_management_system.auth_service.auth_service.enums.UserRole;

public record LoginResponse(
        String jwt,
        String username,
        UserRole role) {}
