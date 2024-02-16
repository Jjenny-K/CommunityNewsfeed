package com.communitynewsfeed.domain.entity;

import com.communitynewsfeed.domain.core.Image;
import com.communitynewsfeed.domain.dto.UpdateProfileImageDto;
import jakarta.persistence.*;
import lombok.*;
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
