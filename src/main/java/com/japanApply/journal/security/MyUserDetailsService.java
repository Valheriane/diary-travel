package com.japanApply.journal.security;

import com.japanApply.journal.model.User;
import com.japanApply.journal.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // RUI => RANG : BASE, PRENIUM => RANG_BASE, RANG_PRENIUM
        // ADMINISTRATOR, PRENIUM, BASE

        // RBAC => RANG BASED ACCESS CONTROL
        // -- Convert user roles to GrantedAuthority objects of spring security
        // -- This is required by spring security to check user roles in security config
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRangType().name()));

        // -- Return a new UserDetails object
        return new org.springframework.security.core.userdetails.User( // This is the building of spring boot PRINCIPAL USER
                user.getEmail(), // Email is our username
                user.getPassword(), // The password of our user
                authorities //
        );
    }
}