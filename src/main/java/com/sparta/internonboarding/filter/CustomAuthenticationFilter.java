package com.sparta.internonboarding.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.internonboarding.dto.request.SignReqDto;
import com.sparta.internonboarding.dto.response.SignResDto;
import com.sparta.internonboarding.enums.JwtTokenType;
import com.sparta.internonboarding.jwt.JwtUtil;
import com.sparta.internonboarding.userdetails.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/sign");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignReqDto reqDto = new ObjectMapper().readValue(request.getInputStream(), SignReqDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            reqDto.getUsername(),
                            reqDto.getPassword(),
                            null
                    )
            );
        } catch(IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        jwtUtil.addTokenToResponse(username, response);

        String accessToken = response
                .getHeader(JwtTokenType.ACCESS_TOKEN.getHeader())
                .substring(JwtUtil.BEARER_PREFIX.length());
        response.getWriter().write(new ObjectMapper().writeValueAsString(new SignResDto(accessToken)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
