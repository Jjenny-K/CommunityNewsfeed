package com.user_service.domain.dto;

import com.user_service.domain.entity.ProfileImage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageResponseDto {

    @NotNull
    private String filePath;

    @NotNull
    private String fileName;

    public static ProfileImageResponseDto from(ProfileImage profileImage) {
        if (profileImage == null) return null;

        return ProfileImageResponseDto.builder()
                .filePath(profileImage.getFilePath())
                .fileName(profileImage.getFileName())
                .build();
    }

}
