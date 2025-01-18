package com.sparta.internonboarding.auth.dto.response;

import com.sparta.internonboarding.user.entity.User;
import lombok.Getter;

@Getter
public class UserRoleResDto {
    private final String authorityName;

    public UserRoleResDto(User user) {
        this.authorityName = user.getUserRole().getAuthority();
    }
}
