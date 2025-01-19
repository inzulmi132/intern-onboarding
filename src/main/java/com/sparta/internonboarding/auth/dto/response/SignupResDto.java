package com.sparta.internonboarding.auth.dto.response;

import com.sparta.internonboarding.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupResDto {
    @Schema(name = "username", description = "계정 이름")
    private final String username;
    @Schema(name = "nickname", description = "계정 닉네임")
    private final String nickname;
    @Schema(name = "authorities", description = "계정 권한 목록")
    private final List<UserRoleResDto> authorities;

    public SignupResDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.authorities = List.of(new UserRoleResDto(user));
    }
}
