package com.ticket_management_system.auth_service.auth_service.security.request;


import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import jakarta.validation.constraints.*;

public record AdminCreateUserRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 128)
        String password,

        @NotBlank
        @Size(max = 80)
        String name,

        @NotNull
        UserRole role,

        @NotBlank
        @Size(max = 120)
        String companyName
) {}
