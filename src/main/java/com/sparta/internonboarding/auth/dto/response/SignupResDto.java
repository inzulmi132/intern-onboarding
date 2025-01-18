package com.sparta.internonboarding.auth.dto.response;

import com.sparta.internonboarding.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupResDto {
    private final String username;
    private final String nickname;
    private final List<UserRoleResDto> authorities;

    public SignupResDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.authorities = List.of(new UserRoleResDto(user));
    }
}
