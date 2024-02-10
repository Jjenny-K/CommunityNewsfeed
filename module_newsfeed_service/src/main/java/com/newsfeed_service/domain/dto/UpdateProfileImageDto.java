package com.newsfeed_service.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileImageDto {

    @NotNull
    private String uuid;

    @NotNull
    private String fileName;

    @NotNull
    private String filePath;

    public static UpdateProfileImageDto from(String uuid, String fileName, String filePath) {
        if(uuid == null) return null;
        if(fileName == null) return null;
        if(filePath == null) return null;

        return UpdateProfileImageDto.builder()
                .uuid(uuid)
                .fileName(fileName)
                .filePath(filePath)
                .build();
    }

}
