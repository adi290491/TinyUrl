package com.tinyurl.api.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyurl.api.userservice.model.LoginRequestModel;
import com.tinyurl.api.userservice.service.UserManagementService;
import com.tinyurl.api.userservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserManagementService userManagementService;
    private final JwtUtil jwtUtil;
    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserManagementService userManagementService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userManagementService = userManagementService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();

        UserDetails userDetails =  userManagementService.loadUserByUsername(username);


    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, exception);
    }
}
