package com.sparta.internonboarding.auth.filter;

import com.sparta.internonboarding.auth.jwt.JwtTokenType;
import com.sparta.internonboarding.auth.jwt.JwtUtil;
import com.sparta.internonboarding.auth.userdetails.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenFromRequest(request, JwtTokenType.ACCESS_TOKEN);
        if(!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!jwtUtil.validateToken(accessToken)) {
            String refreshToken = jwtUtil.getTokenFromRequest(request, JwtTokenType.REFRESH_TOKEN);
            if(!StringUtils.hasText(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtUtil.validateRefreshToken(refreshToken, accessToken);

            String username = jwtUtil.getSubjectFromToken(accessToken);
            jwtUtil.addTokenToResponse(username, response);
        }

        String username = jwtUtil.getSubjectFromToken(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
