package com.sparta.internonboarding.auth.controller;

import com.sparta.internonboarding.auth.dto.request.SignupReqDto;
import com.sparta.internonboarding.auth.dto.response.SignupResDto;
import com.sparta.internonboarding.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> signup(@RequestBody SignupReqDto repDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(repDto));
    }
}
