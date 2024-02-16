package com.communitynewsfeed.repository;

import com.communitynewsfeed.domain.entity.CustomUser;
import com.communitynewsfeed.domain.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByUser(CustomUser user);

}
