package com.ticket_management_system.auth_service.auth_service.services.serviceimpl;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;
import com.ticket_management_system.auth_service.auth_service.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Email not registered : "+email));
    }
}
