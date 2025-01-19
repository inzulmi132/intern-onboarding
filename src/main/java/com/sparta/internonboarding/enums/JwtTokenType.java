package com.sparta.internonboarding.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenType {
    ACCESS_TOKEN(JwtHeaders.ACCESS_TOKEN, ExpireTime.ACCESS_TOKEN),
    REFRESH_TOKEN(JwtHeaders.REFRESH_TOKEN, ExpireTime.REFRESH_TOKEN);

    private final String header;
    private final long expirationTime;

    public static class JwtHeaders {
        private static final String ACCESS_TOKEN = "Authorization";
        private static final String REFRESH_TOKEN = "refresh_token";
    }

    public static class ExpireTime {
        public static final long ACCESS_TOKEN = 1000 * 60 * 30; // 30분
        public static final long REFRESH_TOKEN = 1000 * 60 * 60 * 3; // 3시간
    }
}
