package com.sparta.internonboarding.service;

import com.sparta.internonboarding.dto.request.SignupReqDto;
import com.sparta.internonboarding.dto.response.SignupResDto;
import com.sparta.internonboarding.dto.response.TestResDto;
import com.sparta.internonboarding.entity.User;
import com.sparta.internonboarding.exception.CustomApiException;
import com.sparta.internonboarding.exception.ErrorCode;
import com.sparta.internonboarding.repository.UserRepository;
import com.sparta.internonboarding.userdetails.UserDetailsImpl;
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
            throw new CustomApiException(ErrorCode.ALREADY_EXIST_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());
        User user = reqDto.toUser(encodedPassword);
        return new SignupResDto(userRepository.save(user));
    }

    public TestResDto test(UserDetailsImpl userDetails) {
        return new TestResDto(userDetails.getUser());
    }
}
