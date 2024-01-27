package com.handicraft.service;

import com.handicraft.domain.dto.UserRequestDto;
import com.handicraft.domain.dto.UserResponseDto;
import com.handicraft.domain.entity.Authority;
import com.handicraft.domain.entity.CustomUser;
import com.handicraft.repository.UserRepository;
import com.handicraft.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @Transactional
    public UserRequestDto signup(UserRequestDto userDto) {

        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder().authorityName("ROLE_USER").build();

        CustomUser user = CustomUser.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .greeting(userDto.getGreeting())
                .isActivated(true)
                .authorities(Collections.singleton(authority))
                .build();

        return UserRequestDto.from(userRepository.save(user));
    }

    // 본인 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo() {
        return UserResponseDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new RuntimeException("확인할 수 없습니다."))
        );
    }

}
