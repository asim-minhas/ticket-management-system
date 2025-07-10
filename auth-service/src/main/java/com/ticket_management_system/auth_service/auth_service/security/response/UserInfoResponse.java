package com.ticket_management_system.auth_service.auth_service.security.response;


import com.ticket_management_system.auth_service.auth_service.enums.UserRole;

/**
 * Returned by GET /api/auth/admin/userinfo (and similar “who-am-I” endpoints).
 */
public record UserInfoResponse(
        String email,
        UserRole role
) {}
