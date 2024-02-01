package com.handicraft.service;

import com.handicraft.domain.dto.ProfileImageUploadDto;
import com.handicraft.domain.dto.UpdatePasswordDto;
import com.handicraft.domain.dto.UserResponseDto;
import com.handicraft.domain.dto.UpdateUserDto;
import com.handicraft.domain.entity.CustomUser;
import com.handicraft.repository.UserRepository;
import com.handicraft.util.SecurityUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       ImageService imageService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    // 본인 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMyUserInfo() {
        return UserResponseDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                        .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."))
        );
    }

    // 본인 정보 수정
    @Transactional
    public void updateMyUserInfo(UpdateUserDto updateUserDto) throws IOException {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        user.updateUserInfo(updateUserDto);

        if (updateUserDto.getProfileImage() != null &&
                !Objects.equals(updateUserDto.getProfileImage().getOriginalFilename(), "")) {
            if (!Objects.requireNonNull(updateUserDto.getProfileImage().getContentType()).startsWith("image")) {
                throw new RuntimeException("이미지 파일이 아닙니다.");
            }

            imageService.updateProfileImage(new ProfileImageUploadDto(updateUserDto.getProfileImage()), user.getEmail());
        }
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        CustomUser user = validatePassword(updatePasswordDto.getCurrentPassword());

        user.updatePassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    // 비밀번호 확인
    public CustomUser validatePassword(String currentPassword) {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

}
