package com.sparta.internonboarding.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    @Mock
    JwtTokenType mockJwtTokenType;
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

    @Test
    @DisplayName("만료된 Token 구분 테스트")
    void test3() {
        // given
        String username = "JIN HO";
        when(mockJwtTokenType.getExpirationTime())
                .thenReturn(1000L * -1)
                .thenReturn(1000L);

        // when
        String invalidToken = jwtUtil.generateToken(username, mockJwtTokenType);
        boolean invalidResult = jwtUtil.validateToken(invalidToken);

        String validToken = jwtUtil.generateToken(username, mockJwtTokenType);
        boolean validResult = jwtUtil.validateToken(validToken);

        // then
        assertFalse(invalidResult);
        assertTrue(validResult);
    }
}
