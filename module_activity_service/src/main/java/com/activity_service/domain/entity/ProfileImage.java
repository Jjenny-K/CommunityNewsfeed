package com.activity_service.domain.entity;

import com.activity_service.domain.core.Image;
import com.activity_service.domain.dto.UpdateProfileImageDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "profileImages")
@Table(name = "profileImages")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProfileImage extends Image {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private CustomUser user;

    public void updateProfileImage(UpdateProfileImageDto updateProfileImageDto) {
        this.updateImage(updateProfileImageDto.getUuid(),
                         updateProfileImageDto.getFileName(),
                         updateProfileImageDto.getFilePath());
    }

}
