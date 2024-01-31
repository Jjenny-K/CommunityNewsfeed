package com.handicraft.service;

import com.handicraft.domain.dto.ProfileImageUploadDto;
import com.handicraft.domain.entity.CustomUser;
import com.handicraft.domain.entity.ProfileImage;
import com.handicraft.repository.ProfileImageRepository;
import com.handicraft.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        CustomUser user = userRepository.findOneWithAuthoritiesWithProFileImageByEmail(email)
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

}
