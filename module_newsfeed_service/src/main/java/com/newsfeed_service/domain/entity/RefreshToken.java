package com.newsfeed_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
