package com.ticket_management_system.auth_service.auth_service.ci;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@TestConfiguration
@RestController
@RequestMapping("/api/auth")
public class CiStubController {

  // POST /api/auth/public/signup
  @PostMapping("/public/signup")
  public ResponseEntity<?> signup(@RequestBody Map<String, Object> body) {
    String email = String.valueOf(body.get("email"));
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "jwt", "stub.jwt." + UUID.randomUUID(),
      "username", email,
      "role", "CLIENT_ADMIN"
    ));
  }

  // POST /api/auth/public/signin
  @PostMapping("/public/signin")
  public ResponseEntity<?> signin(@RequestBody Map<String, Object> body) {
    String email = String.valueOf(body.get("email"));
    String role  = email != null && email.toLowerCase().contains("admin") ? "ADMIN" : "CLIENT_ADMIN";
    return ResponseEntity.ok(Map.of(
      "jwt", "stub.jwt." + UUID.randomUUID(),
      "username", email,
      "role", role
    ));
  }

  // GET /protected/endpoint  (note: NOT under /api/auth in your codebase)
  @GetMapping("/../protected/endpoint") // resolves to /protected/endpoint
  public ResponseEntity<?> protectedEndpoint(@RequestHeader(value = "Authorization", required = false) String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing token"));
    }
    return ResponseEntity.ok("Protected content");
  }

  // POST /api/auth/admin/users
  @PostMapping("/admin/users")
  public ResponseEntity<?> adminUsers(@RequestBody Map<String, Object> body,
                                      @RequestHeader(value = "Authorization", required = false) String auth) {
    // ultra-simple check: any header present → allow create
    if (auth == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Forbidden"));
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "id", UUID.randomUUID().toString(),
      "email", body.get("email"),
      "role", body.get("role")
    ));
  }
}
