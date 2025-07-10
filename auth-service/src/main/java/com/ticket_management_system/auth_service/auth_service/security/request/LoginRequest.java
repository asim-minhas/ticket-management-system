package com.ticket_management_system.auth_service.auth_service.security.request;

public record LoginRequest(
        String email,
        String password) {}
