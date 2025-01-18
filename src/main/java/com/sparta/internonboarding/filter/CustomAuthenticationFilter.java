package com.sparta.internonboarding.filter;

import com.sparta.internonboarding.jwt.JwtUtil;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.setFilterProcessesUrl("/sign");
    }
}
