package com.sparta.internonboarding.auth.dto.request;

import com.sparta.internonboarding.user.entity.User;
import com.sparta.internonboarding.user.enums.UserRole;
import lombok.Getter;

@Getter
public class SignupReqDto {
    private String username;
    private String password;
    private String nickname;

    public User toUser(String encodedPassword) {
        return User.builder()
                .username(this.username)
                .password(encodedPassword)
                .nickname(this.nickname)
                .userRole(UserRole.USER)
                .build();
    }
}
