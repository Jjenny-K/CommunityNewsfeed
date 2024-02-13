package com.activity_service.repository;

import com.activity_service.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM follows f WHERE f.followerUserId = :from AND f.followingUserId = :to")
    Optional<Follow> findFollow(@Param("from") long followerUserId, @Param("to") long followingUserId);
    List<Follow> findByFollowerUserId(long followerUserId);
    List<Follow> findByFollowingUserId(long followingUserId);

}
