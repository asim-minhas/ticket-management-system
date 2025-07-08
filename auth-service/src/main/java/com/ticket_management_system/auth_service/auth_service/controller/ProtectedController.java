package com.ticket_management_system.auth_service.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
public class ProtectedController {
    
    @GetMapping("/endpoint")
    public ResponseEntity<String> getProtectedContent() {
        return ResponseEntity.ok("Protected content");
    }
}