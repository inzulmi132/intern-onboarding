package com.sparta.internonboarding.auth.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignReqDto {
    private String username;
    private String password;
}
