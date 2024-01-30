package com.handicraft.service;

import com.handicraft.domain.dto.UserResponseDto;
import com.handicraft.repository.UserRepository;
import com.handicraft.util.SecurityUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 본인 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo() {
        return UserResponseDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."))
        );
    }

}
