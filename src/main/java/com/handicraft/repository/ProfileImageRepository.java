package com.handicraft.repository;

import com.handicraft.domain.entity.CustomUser;
import com.handicraft.domain.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByUser(CustomUser user);

}
