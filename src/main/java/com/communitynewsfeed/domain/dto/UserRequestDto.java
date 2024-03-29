package com.communitynewsfeed.domain.dto;

import com.communitynewsfeed.domain.entity.CustomUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
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

    public static UserRequestDto from(CustomUser user) {
        if(user == null) return null;

        return UserRequestDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .greeting(user.getGreeting())
                .build();
    }

}
