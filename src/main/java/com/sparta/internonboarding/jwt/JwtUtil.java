package com.sparta.internonboarding.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3; // 3시간

    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        return generateToken(subject, expiration);
    }

    public String generateRefreshToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
        return generateToken(subject, expiration);
    }

    public String generateToken(String subject, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch(ExpiredJwtException e) {
            throw new RuntimeException("만료된 JWT token 입니다.");
        } catch(SecurityException | MalformedJwtException e) {
            throw new RuntimeException("유효하지 않는 JWT 서명 입니다.");
        } catch(UnsupportedJwtException e) {
            throw new RuntimeException("지원되지 않는 JWT 토큰 입니다.");
        } catch(IllegalArgumentException e) {
            throw new RuntimeException("잘못된 JWT 토큰 입니다.");
        }
    }
}
