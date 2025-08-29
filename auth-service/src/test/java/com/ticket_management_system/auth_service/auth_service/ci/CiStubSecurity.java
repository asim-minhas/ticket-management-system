package com.ticket_management_system.auth_service.auth_service.ci;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class CiStubSecurity {

  // Single permissive chain; no JWT, no sessions, no CSRF
  @Primary
  @Order(0)
  @org.springframework.context.annotation.Bean
  SecurityFilterChain testSecurity(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .build();
  }
}
