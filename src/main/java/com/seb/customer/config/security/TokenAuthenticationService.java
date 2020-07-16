package com.seb.customer.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class TokenAuthenticationService {

    public static final String AUTH_COOKIE_NAME = "AUTHORIZATION";

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public TokenAuthenticationService(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    public void addAuthentication(HttpServletResponse response,
                                  UserAuthentication authentication) throws IOException {
        final UserDetails user = authentication.getDetails();
        final String token = jwtUtils.generateToken(user);

        response.addCookie(createCookieForToken(token));
        String loginUser = new ObjectMapper().writeValueAsString(new LoginUser(user.getUsername(), token));
        response.setContentType("application/json");
        response.getWriter().write(loginUser);
    }

    public Optional<UserAuthentication> getAuthentication(HttpServletRequest request) {

        final String token = request.getHeader(AUTH_COOKIE_NAME);
        if (nonNull(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            UserDetails user = customUserDetailsService.loadUserByUsername(username);
            if (jwtUtils.isTokenValid(token, user)) {
                return Optional.of(new UserAuthentication(user));
            }
        }
        return Optional.empty();
    }

    private Cookie createCookieForToken(String token) {
        final Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, token);
        authCookie.setPath("/");
        return authCookie;
    }

    private static class LoginUser {
        private String username;
        private String token;

        public LoginUser() {
        }

        public LoginUser(String username, String token) {
            this.username = username;
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public String getToken() {
            return token;
        }
    }
}

