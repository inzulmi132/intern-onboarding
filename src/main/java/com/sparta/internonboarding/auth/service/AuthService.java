package com.sparta.internonboarding.auth.service;

import com.sparta.internonboarding.auth.dto.request.SignupReqDto;
import com.sparta.internonboarding.auth.dto.response.SignupResDto;
import com.sparta.internonboarding.auth.dto.response.TestResDto;
import com.sparta.internonboarding.auth.userdetails.UserDetailsImpl;
import com.sparta.internonboarding.user.entity.User;
import com.sparta.internonboarding.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResDto signup(SignupReqDto reqDto) {
        if(userRepository.existsByUsername(reqDto.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 이름 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());
        User user = reqDto.toUser(encodedPassword);
        return new SignupResDto(userRepository.save(user));
    }

    public TestResDto test(UserDetailsImpl userDetails) {
        return new TestResDto(userDetails.getUser());
    }
}
