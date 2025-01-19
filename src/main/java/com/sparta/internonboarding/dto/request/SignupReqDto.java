package com.sparta.internonboarding.dto.request;

import com.sparta.internonboarding.entity.User;
import com.sparta.internonboarding.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupReqDto {
    @Schema(name = "username", description = "계정 이름")
    private String username;
    @Schema(name = "password", description = "계정 비밀 번호")
    private String password;
    @Schema(name = "nickname", description = "계정 닉네임")
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
