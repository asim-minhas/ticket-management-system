package com.ticket_management_system.auth_service.auth_service.controller;

import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import com.ticket_management_system.auth_service.auth_service.security.jwt.JwtUtils;
import com.ticket_management_system.auth_service.auth_service.security.request.LoginRequest;
import com.ticket_management_system.auth_service.auth_service.security.response.LoginResponse;
import com.ticket_management_system.auth_service.auth_service.security.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user '{}'", loginRequest.getEmail());

        try{
            Authentication authentication;

            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("Authentication successful for user '{}'", loginRequest.getEmail());

            String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());
            log.info("Login successful. JWT issued for '{}'", loginRequest.getEmail());

            UserRole userRole = userDetails.getAuthorities().stream().findFirst().map(item -> UserRole.valueOf(item.getAuthority())).orElse(null);
            String username = userDetails.getUsername();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginResponse response = new LoginResponse(jwtToken, username, userRole);
            return ResponseEntity.ok(response);
        }catch (BadCredentialsException e) {
            log.warn("Login failed: Invalid credentials for '{}'", loginRequest.getEmail());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception e) {
            log.error("Unexpected error during login for '{}'", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Something went wrong"));
        }
    }
}
