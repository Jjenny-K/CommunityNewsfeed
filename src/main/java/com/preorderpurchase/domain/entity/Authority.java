package com.preorderpurchase.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Authority {

    @Id
    @Column(name = "authorityName", length = 50)
    private String authorityName;

}