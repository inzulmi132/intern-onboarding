package com.sparta.internonboarding.controller;

import com.sparta.internonboarding.dto.request.SignReqDto;
import com.sparta.internonboarding.dto.request.SignupReqDto;
import com.sparta.internonboarding.dto.response.SignResDto;
import com.sparta.internonboarding.dto.response.SignupResDto;
import com.sparta.internonboarding.dto.response.TestResDto;
import com.sparta.internonboarding.service.AuthService;
import com.sparta.internonboarding.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "계정 관리", description = "계정 관리 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "사용자 회원 가입 API")
    @Parameter(name = "username", description = "계정 이름", example = "JIN HO")
    @Parameter(name = "password", description = "계정 비밀 번호", example = "12341234")
    @Parameter(name = "nickname", description = "계정 닉네임", example = "Mentos")
    @ApiResponse(
            responseCode = "201",
            description = "회원 가입 성공",
            content = @Content(
                    schema = @Schema(implementation = SignupResDto.class),
                    mediaType = "application/json"
            )
    )
    public ResponseEntity<SignupResDto> signup(@RequestBody SignupReqDto repDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(repDto));
    }

    @GetMapping("/test")
    @Operation(summary = "테스트", description = "Spring Security Context 테스트 API")
    @ApiResponse(
            responseCode = "200",
            description = "Spring Security Context 생성 및 사용 성공",
            content = @Content(
                    schema = @Schema(implementation = TestResDto.class),
                    mediaType = "application/json"
            )
    )
    public ResponseEntity<TestResDto> test(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.test(userDetails));
    }

    @Operation(summary = "User login")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                    schema = @Schema(implementation = SignResDto.class),
                    mediaType = "application/json"
            )
    )
    @PostMapping("/sign")
    public ResponseEntity<SignResDto> theFakeLogin(@RequestBody SignReqDto reqDto) {
        throw new IllegalStateException("요청할 수 없는 API");
    }
}
