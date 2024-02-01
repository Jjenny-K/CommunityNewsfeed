package com.preorderpurchase.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "rtKey")
    private String key;

    @Column(name = "rtValue")
    private String value;

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

}
