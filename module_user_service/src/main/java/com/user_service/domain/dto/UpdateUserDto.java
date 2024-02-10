package com.user_service.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @Size(min = 3, max =50)
    private String name;

    @Size(min = 3, max =100)
    private String greeting;

}
