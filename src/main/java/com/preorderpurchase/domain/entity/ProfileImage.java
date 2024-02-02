package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.Image;
import com.preorderpurchase.domain.dto.UpdateProfileImageDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
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
        this.setUuid(updateProfileImageDto.getUuid());
        this.setFileName(updateProfileImageDto.getFileName());
        this.setFilePath(updateProfileImageDto.getFilePath());
    }

}
