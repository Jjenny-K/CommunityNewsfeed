package com.newsfeed_service.repository;

import com.newsfeed_service.domain.entity.CustomUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    // email을 기준으로 User 정보를 가져올 권한 정보도 같이 가져옴
    @EntityGraph(attributePaths = "authorities")
    Optional<CustomUser> findOneWithAuthoritiesByEmail(String email);
    @EntityGraph(attributePaths = {"authorities", "profileImage"})
    Optional<CustomUser> findOneWithAuthoritiesWithProFileImageByEmail(String email);

}

