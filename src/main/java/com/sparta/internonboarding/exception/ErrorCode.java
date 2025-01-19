package com.sparta.internonboarding.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // userDetails
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

    // jwt
    INVALID_JWT_SIGN(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다."),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰 입니다."),

    // auth
    UNEXPECTED_API_REQUEST(HttpStatus.FORBIDDEN, "요청할 수 없는 API 입니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 가입된 계정 명 입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
