package com.preorderpurchase.domain.core;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@MappedSuperclass
public abstract class Image extends BaseCreatedUpdated{

    @Id
    @Column(name = "imageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imageUuid", nullable = false)
    private String uuid;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "filePath", nullable = false)
    private String filePath;

    protected void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
