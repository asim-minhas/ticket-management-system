package com.ticket_management_system.auth_service.auth_service.security.request;

import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminCreateUserRequest(
        @Email String email,
        @Size(min = 8)       String password,
        @NotBlank String name,
        @NotNull UserRole role) { }