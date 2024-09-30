package com.tinyurl.api.userservice.config;

import com.tinyurl.api.userservice.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserManagementService userManagementService;

    @Value("${gateway.ip}")
    private String gatewayIP;

    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, UserManagementService userManagementService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userManagementService = userManagementService;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userManagementService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/users/**")
                                                .access(new WebExpressionAuthorizationManager("hasIpAddress('" + gatewayIP + "')"))
                        .anyRequest().authenticated())
                .addFilter(new AuthenticationFilter(authenticationManager, null))
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
