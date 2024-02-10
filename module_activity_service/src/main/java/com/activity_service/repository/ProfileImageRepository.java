package com.activity_service.repository;

import com.activity_service.domain.entity.CustomUser;
import com.activity_service.domain.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByUser(CustomUser user);

}
