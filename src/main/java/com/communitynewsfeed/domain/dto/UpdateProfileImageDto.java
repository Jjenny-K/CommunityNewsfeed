package com.communitynewsfeed.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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
