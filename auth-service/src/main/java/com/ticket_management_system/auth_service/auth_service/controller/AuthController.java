package com.ticket_management_system.auth_service.auth_service.controller;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import com.ticket_management_system.auth_service.auth_service.enums.UserRole;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;
import com.ticket_management_system.auth_service.auth_service.security.jwt.JwtUtils;
import com.ticket_management_system.auth_service.auth_service.security.request.LoginRequest;
import com.ticket_management_system.auth_service.auth_service.security.request.SignUpRequest;
import com.ticket_management_system.auth_service.auth_service.security.response.LoginResponse;
import com.ticket_management_system.auth_service.auth_service.security.response.SignUpResponse;
import com.ticket_management_system.auth_service.auth_service.security.service.UserDetailsImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user '{}'", loginRequest.email());

        try{
            Authentication authentication;

            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
            UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("Authentication successful for user '{}'", loginRequest.email());

            String jwtToken = jwtUtils.generateJwtToken(userDetails.getUsername());
            log.info("Login successful. JWT issued for '{}'", loginRequest.email());

            UserRole userRole = userDetails.getAuthorities().stream().findFirst().map(item -> UserRole.valueOf(item.getAuthority())).orElse(null);
            String username = userDetails.getUsername();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginResponse response = new LoginResponse(jwtToken, username, userRole);
            return ResponseEntity.ok(response);
        }catch (BadCredentialsException e) {
            log.warn("Login failed: Invalid credentials for '{}'", loginRequest.email());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception e) {
            log.error("Unexpected error during login for '{}'", loginRequest.email(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Something went wrong"));
        }
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            log.info("Signup attempt for user '{}'", signUpRequest.email());

            if (userRepository.existsByEmail(signUpRequest.email())) {
                log.warn("Signup failed: Email already registered '{}'", signUpRequest.email());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Email already registered"));
            }
            User user = new User();
            user.setEmail(signUpRequest.email());
            user.setPassword(passwordEncoder.encode(signUpRequest.password()));
            user.setName(signUpRequest.name());
            user.setCompanyName(signUpRequest.companyName());
            user.setRole(UserRole.CLIENT_ADMIN);
            user.setCreatedAt(LocalDateTime.now());

            userRepository.save(user);

            String jwt = jwtUtils.generateJwtToken(user.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SignUpResponse(jwt, user.getEmail(), UserRole.CLIENT_ADMIN.name()));
    }catch (ConstraintViolationException ex){
            log.warn("Signup failed: Invalid credentials for '{}'", signUpRequest.email());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", ex.getMessage()));
        }
        catch(Exception e){
            log.error("Signup failed for '{}'", signUpRequest.email(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Something went wrong"));
        }
    }
}
