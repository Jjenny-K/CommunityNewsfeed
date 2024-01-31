package com.handicraft.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.handicraft.domain.entity.CustomUser;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max =50)
    private String name;

    @NotNull
    @Size(min = 3, max =100)
    private String greeting;

    @NotNull
    private MultipartFile profileImage;

    public static UserRequestDto from(CustomUser user) {
        if(user == null) return null;

        return UserRequestDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .greeting(user.getGreeting())
                .build();
    }

}
