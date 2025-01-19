package com.sparta.internonboarding.dto.response;

import com.sparta.internonboarding.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserRoleResDto {
    @Schema(name = "authorityName", description = "권한 이름")
    private final String authorityName;

    public UserRoleResDto(User user) {
        this.authorityName = user.getUserRole().getAuthority();
    }
}
