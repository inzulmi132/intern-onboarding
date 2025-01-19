package com.sparta.internonboarding.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomErrorResDto {

    private final String msg;
    private final HttpStatus status;

    public CustomErrorResDto(CustomApiException e) {
        this.msg = e.getErrorCode().getMessage();
        this.status = e.getErrorCode().getStatus();
    }
}
