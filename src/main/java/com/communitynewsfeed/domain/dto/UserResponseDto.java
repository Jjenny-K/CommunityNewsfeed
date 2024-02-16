package com.communitynewsfeed.domain.dto;

import com.communitynewsfeed.domain.entity.CustomUser;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    @Size(min = 3, max =50)
    private String name;

    @NotNull
    @Size(min = 3, max =100)
    private String greeting;

    @NotNull
    private ProfileImageResponseDto profileImage;

    public static UserResponseDto from(CustomUser user) {
        if(user == null) return null;

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .greeting(user.getGreeting())
                .profileImage(ProfileImageResponseDto.from(user.getProfileImage()))
                .build();
    }

}
