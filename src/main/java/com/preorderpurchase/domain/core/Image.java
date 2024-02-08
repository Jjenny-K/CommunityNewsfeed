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
    @Column(name = "imageId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imageUuid", nullable = false)
    private String uuid;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Column(name = "filePath", nullable = false)
    private String filePath;

    public void updateImage(String uuid, String fileName, String filePath) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
