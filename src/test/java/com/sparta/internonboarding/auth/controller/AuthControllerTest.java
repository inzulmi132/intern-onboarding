package com.sparta.internonboarding.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sparta.internonboarding.auth.dto.request.SignReqDto;
import com.sparta.internonboarding.auth.dto.request.SignupReqDto;
import com.sparta.internonboarding.auth.jwt.JwtTokenType;
import com.sparta.internonboarding.auth.jwt.JwtUtil;
import com.sparta.internonboarding.user.entity.User;
import com.sparta.internonboarding.user.enums.UserRole;
import com.sparta.internonboarding.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private final String username = "JIN HO";
    private final String password = "12341234";
    private final String nickname = "Mentos";
    @Mock
    JwtTokenType mockJwtTokenType;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void test1() throws Exception {
        // given
        SignupReqDto reqDto = new SignupReqDto(username, password, nickname);

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reqDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        LinkedHashMap<String, Object> responseMap = JsonPath.parse(result.getResponse().getContentAsString()).read("$");

        // then
        assertNotNull(responseMap);
        assertEquals(username, responseMap.get("username"));
        assertEquals(nickname, responseMap.get("nickname"));
    }

    @Test
    @DisplayName("Access / Refresh Token 유저 정보 검증")
    void test2() throws Exception {
        // given
        userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .nickname(nickname)
                        .userRole(UserRole.USER)
                        .build()
        );
        SignReqDto reqDto = new SignReqDto(username, password);

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/sign")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reqDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String accessToken = result.getResponse().getHeader(JwtTokenType.ACCESS_TOKEN.getHeader());
        if(accessToken != null) {
            accessToken = accessToken.substring(JwtUtil.BEARER_PREFIX.length());
        }
        String refreshToken = result.getResponse().getHeader(JwtTokenType.REFRESH_TOKEN.getHeader());
        if(refreshToken != null) {
            refreshToken = refreshToken.substring(JwtUtil.BEARER_PREFIX.length());
        }

        // then
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        assertEquals(username, jwtUtil.getSubjectFromToken(accessToken));
        assertEquals(username, jwtUtil.getSubjectFromToken(refreshToken));
    }

    @Test
    @DisplayName("Access Token 만료 시 Refresh Token 검증 후 Token 재발행")
    void test3() throws Exception {
        // given
        when(mockJwtTokenType.getExpirationTime()).thenReturn(1000L * -1);
        String invalidAccessToken = jwtUtil.generateToken(username, mockJwtTokenType);
        String refreshToken = jwtUtil.generateToken(username, JwtTokenType.REFRESH_TOKEN);

        userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .nickname(nickname)
                        .userRole(UserRole.USER)
                        .build()
        );

        // refresh token 의 재발급 확인을 위한 대기 시간
        Thread.sleep(500);

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/test")
                                .header(JwtTokenType.ACCESS_TOKEN.getHeader(), JwtUtil.BEARER_PREFIX + invalidAccessToken)
                                .header(JwtTokenType.REFRESH_TOKEN.getHeader(), JwtUtil.BEARER_PREFIX + refreshToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String newAccessToken = result.getResponse().getHeader(JwtTokenType.ACCESS_TOKEN.getHeader());
        if(newAccessToken != null) {
            newAccessToken = newAccessToken.substring(JwtUtil.BEARER_PREFIX.length());
        }
        String newRefreshToken = result.getResponse().getHeader(JwtTokenType.REFRESH_TOKEN.getHeader());
        if(newRefreshToken != null) {
            newRefreshToken = newRefreshToken.substring(JwtUtil.BEARER_PREFIX.length());
        }

        // then
        assertNotNull(newAccessToken);
        assertNotNull(newRefreshToken);
        assertNotEquals(invalidAccessToken, newAccessToken);
        assertNotEquals(refreshToken, newRefreshToken);
    }
}
