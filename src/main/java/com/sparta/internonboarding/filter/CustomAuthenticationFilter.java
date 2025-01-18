package com.sparta.internonboarding.filter;

import com.sparta.internonboarding.jwt.JwtUtil;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration configuration;

    public CustomAuthenticationFilter(JwtUtil jwtUtil, AuthenticationConfiguration configuration) throws Exception {
        this.jwtUtil = jwtUtil;
        this.configuration = configuration;
        this.setAuthenticationManager(configuration.getAuthenticationManager());
        this.setFilterProcessesUrl("/sign");
    }
}
