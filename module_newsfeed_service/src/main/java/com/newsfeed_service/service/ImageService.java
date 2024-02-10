package com.newsfeed_service.service;

import com.newsfeed_service.domain.dto.ProfileImageUploadDto;
import com.newsfeed_service.domain.dto.UpdateProfileImageDto;
import com.newsfeed_service.domain.entity.CustomUser;
import com.newsfeed_service.domain.entity.ProfileImage;
import com.newsfeed_service.repository.ProfileImageRepository;
import com.newsfeed_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.UUID;


@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ProfileImageRepository profileImageRepository;
    private final UserRepository userRepository;

    public ImageService(ProfileImageRepository profileImageRepository,
                        UserRepository userRepository) {
        this.profileImageRepository = profileImageRepository;
        this.userRepository = userRepository;
    }

    @Value("${spring.servlet.multipart.location}")
    private String uploadFolder;

    // 프로필 이미지 업로드
    @Transactional
    public void profileImageUpload(ProfileImageUploadDto profileImageUploadDto, String email) throws IOException {
        CustomUser user = userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        MultipartFile file = profileImageUploadDto.getFile();

        String uuid = UUID.randomUUID().toString();
        String profileImageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + "\\profileImages\\" + profileImageFileName);
        file.transferTo(destinationFile);

        ProfileImage image = ProfileImage.builder()
                    .uuid(uuid)
                    .fileName(file.getOriginalFilename())
                    .filePath("\\profileImages\\" + profileImageFileName)
                    .user(user)
                    .build();

        profileImageRepository.save(image);
    }

    // 프로필 이미지 수정
    @Transactional
    public void updateProfileImage(ProfileImageUploadDto profileImageUploadDto, String email) throws IOException {
        CustomUser user = userRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        MultipartFile file = profileImageUploadDto.getFile();

        String uuid = UUID.randomUUID().toString();
        String profileImageFileName = uuid + "_" + file.getOriginalFilename();
        String profileImageFilePath = "\\profileImages\\" + profileImageFileName;

        ProfileImage image = profileImageRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("이미지가 존재하지 않습니다."));

        if (!deleteFile(image.getFilePath())) {
            throw new RuntimeException("이미지가 존재하지 않습니다.");
        }

        File destinationFile = new File(uploadFolder + profileImageFilePath);
        file.transferTo(destinationFile);

        UpdateProfileImageDto updateProfileImageDto =
                UpdateProfileImageDto.from(uuid, file.getOriginalFilename(), profileImageFilePath);

        image.updateProfileImage(Objects.requireNonNull(updateProfileImageDto));

        profileImageRepository.save(image);
    }

    // 저장된 파일 삭제
    public Boolean deleteFile(String filePath) throws UnsupportedEncodingException {
        File file = new File(uploadFolder + filePath);

        return file.delete();
    }

}
