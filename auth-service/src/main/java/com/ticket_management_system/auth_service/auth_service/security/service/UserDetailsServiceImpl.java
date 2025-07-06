package com.ticket_management_system.auth_service.auth_service.security.service;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ticket_management_system.auth_service.auth_service.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Email not registered : "+username));
        return UserDetailsImpl.createUser(user);
    }
}
