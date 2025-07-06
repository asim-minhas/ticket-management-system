package com.ticket_management_system.auth_service.auth_service.services;

import com.ticket_management_system.auth_service.auth_service.entities.User;

public interface UserService {
    public User findUserByEmail(String email);
}
