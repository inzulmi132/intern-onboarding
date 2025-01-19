package com.sparta.internonboarding.auth.dto.response;

import com.sparta.internonboarding.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TestResDto {
    @Schema(name = "username", description = "Spring Security Context 계정 이름")
    private final String username;
    @Schema(name = "nickname", description = "Spring Security Context 계정 닉네임")
    private final String nickname;

    public TestResDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
