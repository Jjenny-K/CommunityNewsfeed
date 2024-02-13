package com.activity_service.repository;

import com.activity_service.domain.entity.Post;
import com.activity_service.domain.entity.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {

    @Query("SELECT ph FROM postHearts ph WHERE ph.userId = :userId AND ph.post = :post")
    Optional<PostHeart> findPostHeart(@Param("userId") long userId, @Param("post") Post post);
    List<PostHeart> findByUserId(long userId);
    List<PostHeart> findByPost(Post post);

}
