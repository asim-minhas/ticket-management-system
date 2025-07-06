package com.ticket_management_system.auth_service.auth_service.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticket_management_system.auth_service.auth_service.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;

    @JsonIgnore
    private String password;

    //private boolean is2faEnabled;

    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public static UserDetailsImpl createUser(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());

        return UserDetailsImpl.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(user.getEmail())
                .authorities(List.of(grantedAuthority))
                .build();
    }
}
