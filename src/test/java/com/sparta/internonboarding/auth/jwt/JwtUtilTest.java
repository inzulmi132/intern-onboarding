package com.sparta.internonboarding.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String secretKey = "InternOnboardingTestSecretKey1234567890";
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        jwtUtil.init();
    }

    @Test
    @DisplayName("Access Token 발행 테스트")
    void test1() {
        // given
        String username = "JIN HO";

        // when
        String accessToken = jwtUtil.generateToken(username, JwtTokenType.ACCESS_TOKEN);
        String tokenUsername = jwtUtil.getSubjectFromToken(accessToken);

        // then
        assertNotNull(accessToken);
        assertTrue(jwtUtil.validateToken(accessToken));
        assertEquals(username, tokenUsername);
    }

    @Test
    @DisplayName("Refresh Token 발행 테스트")
    void test2() {
        // given
        String username = "JIN HO";

        // when
        String refreshToken = jwtUtil.generateToken(username, JwtTokenType.REFRESH_TOKEN);
        String tokenUsername = jwtUtil.getSubjectFromToken(refreshToken);

        // then
        assertNotNull(refreshToken);
        assertTrue(jwtUtil.validateToken(refreshToken));
        assertEquals(username, tokenUsername);
    }
}
