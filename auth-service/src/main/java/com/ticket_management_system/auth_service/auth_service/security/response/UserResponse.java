package com.ticket_management_system.auth_service.auth_service.security.response;

import com.ticket_management_system.auth_service.auth_service.enums.UserRole;

public record UserResponse(
        String id,
        String email,
        UserRole role) { }