package com.ticket_management_system.auth_service.auth_service.security.response;

public record SignUpResponse(String jwt, String username, String role) {}
