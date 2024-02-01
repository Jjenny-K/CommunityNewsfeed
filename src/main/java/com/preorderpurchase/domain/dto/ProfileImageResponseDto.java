package com.preorderpurchase.domain.dto;

import com.preorderpurchase.domain.entity.ProfileImage;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
