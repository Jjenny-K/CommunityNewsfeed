package com.preorderpurchase.repository;

import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Post;
import com.preorderpurchase.domain.entity.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {

    @Query("SELECT ph FROM postHearts ph WHERE ph.postHeartUser = :postHeartUser AND ph.post = :post")
    Optional<PostHeart> findPostHeart(@Param("postHeartUser") CustomUser postHeartUser, @Param("post") Post post);

}
