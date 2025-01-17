package com.sparta.internonboarding.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authorityName;

    UserRole(String authorityName) {
        this.authorityName = authorityName;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
