package com.preorderpurchase.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

}
