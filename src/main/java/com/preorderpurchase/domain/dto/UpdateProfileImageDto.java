package com.preorderpurchase.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
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

}
