package com.activity_service.repository;

import com.activity_service.domain.entity.CustomUser;
import com.activity_service.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM follows f WHERE f.followerUser = :from AND f.followingUser = :to")
    Optional<Follow> findFollow(@Param("from") CustomUser followerUser, @Param("to") CustomUser followingUser);
    List<Follow> findByFollowerUser(CustomUser followerUser);
    List<Follow> findByFollowingUser(CustomUser followingUser);

}
