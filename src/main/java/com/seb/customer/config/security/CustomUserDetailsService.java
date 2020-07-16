package com.seb.customer.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final String defaultUsername;
    private final String password;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder, @Value("${default.user.username}") String defaultUsername,
                                    @Value("${default.user.username}") String password) {
        this.passwordEncoder = passwordEncoder;
        this.defaultUsername = defaultUsername;
        this.password = password;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional.of(username).filter(v -> v.equals(defaultUsername))
                .orElseThrow(() -> new UsernameNotFoundException("unidentified user"));

        return new AccountCredentials(username, passwordEncoder.encode(password));
    }
}
