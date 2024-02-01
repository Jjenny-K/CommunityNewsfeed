package com.handicraft.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProfileImageUploadDto {

    @NotNull
    private MultipartFile file;

}
