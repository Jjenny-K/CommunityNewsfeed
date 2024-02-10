package com.activity_service.service;

import com.activity_service.util.SecurityUtil;
import com.activity_service.domain.dto.ProfileImageUploadDto;
import com.activity_service.domain.dto.UpdatePasswordDto;
import com.activity_service.domain.dto.UpdateUserDto;
import com.activity_service.domain.dto.UserResponseDto;
import com.activity_service.domain.entity.CustomUser;
import com.activity_service.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void updateMyUserInfo(UpdateUserDto updateUserDto,
                                 MultipartFile updateProfileImage)
            throws IOException {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        user.updateUserInfo(updateUserDto);

        if (updateProfileImage != null &&
                !Objects.equals(updateProfileImage.getOriginalFilename(), "")) {
            if (!Objects.requireNonNull(updateProfileImage.getContentType()).startsWith("image")) {
                throw new RuntimeException("이미지 파일이 아닙니다.");
            }

            imageService.updateProfileImage(new ProfileImageUploadDto(updateProfileImage), user.getEmail());
        }
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        CustomUser user = validatePassword(updatePasswordDto.getCurrentPassword());

        user.updatePassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    // 비밀번호 확인
    @Transactional(readOnly = true)
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
