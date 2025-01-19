package com.sparta.internonboarding.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignResDto {
    @Schema(name = "token", description = "발급된 토큰")
    private final String token;
}
