package com.codeemma.employee.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler {
    public static void handleSuccessLogin(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication, UserDetailsService userService,
                                          TokenAuthenticationService tokenAuthenticationService) throws ServletException, IOException {
        // Lookup the complete User object from the database and create an Authentication for it
        final UserDetails authenticatedUser = userService.loadUserByUsername(authentication.getName());

        // Add UserAuthentication to the response
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
    }
}
