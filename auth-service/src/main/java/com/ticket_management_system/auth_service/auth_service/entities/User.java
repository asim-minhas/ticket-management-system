package com.ticket_management_system.auth_service.auth_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Document("user")
public class User{
    @Id
    private String id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Field("username")
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    private UserRole role;

    @CreatedDate
    @Field("created_at")
    LocalDate createdAt;

    @LastModifiedDate
    @Field("modified_at")
    LocalDate modifiedAt;

    @Field("account_non_locked")
    private boolean accountNonLocked = true;
    @Field("account_non_expired")
    private boolean accountNonExpired = true;
    @Field("credentials_non_expired")
    private boolean credentialsNonExpired = true;
    @Field("enabled")
    private boolean enabled = true;

    //Later implementation for complex user entity
//    private LocalDate credentialsExpiryDate;
//    private LocalDate accountExpiryDate;
//
//    private String twoFactorSecret;
//    private boolean isTwoFactorEnabled = false;
//    private String signUpMethod;

}
