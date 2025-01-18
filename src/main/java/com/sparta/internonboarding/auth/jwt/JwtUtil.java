package com.sparta.internonboarding.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String subject, JwtTokenType tokenType) {
        Date expiration = new Date(new Date().getTime() + tokenType.getExpirationTime());
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void addTokenToResponse(String username, HttpServletResponse response) {
        String accessToken = generateToken(username, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = generateToken(username, JwtTokenType.REFRESH_TOKEN);

        response.addHeader(JwtTokenType.ACCESS_TOKEN.getHeader(), BEARER_PREFIX + accessToken);
        response.addHeader(JwtTokenType.REFRESH_TOKEN.getHeader(), BEARER_PREFIX + refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e) {
            return false;
        } catch(SecurityException | MalformedJwtException e) {
            throw new RuntimeException("유효하지 않는 JWT 서명 입니다.");
        } catch(UnsupportedJwtException e) {
            throw new RuntimeException("지원되지 않는 JWT 토큰 입니다.");
        } catch(IllegalArgumentException e) {
            throw new RuntimeException("잘못된 JWT 토큰 입니다.");
        }
    }

    public String getTokenFromRequest(HttpServletRequest request, JwtTokenType tokenType) {
        String bearerToken = request.getHeader(tokenType.getHeader());
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String getSubjectFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch(ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public void validateRefreshToken(String refreshToken, String accessToken) {
        if(!validateToken(refreshToken)) {
            throw new RuntimeException("만료된 JWT 토큰 입니다.");
        }

        String refreshTokenUsername = getSubjectFromToken(refreshToken);
        String accessTokenUsername = getSubjectFromToken(accessToken);
        if(!Objects.equals(refreshTokenUsername, accessTokenUsername)) {
            throw new IllegalArgumentException("잘못된 JWT 토큰 입니다.");
        }
    }
}
