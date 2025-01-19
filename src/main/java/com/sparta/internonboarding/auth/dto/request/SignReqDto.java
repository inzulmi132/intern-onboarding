package com.sparta.internonboarding.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignReqDto {
    @Schema(name = "username", description = "계정 이름")
    private String username;
    @Schema(name = "password", description = "계정 비밀 번호")
    private String password;
}
