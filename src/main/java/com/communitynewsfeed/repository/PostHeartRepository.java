package com.communitynewsfeed.repository;

import com.communitynewsfeed.domain.entity.CustomUser;
import com.communitynewsfeed.domain.entity.Post;
import com.communitynewsfeed.domain.entity.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {

    @Query("SELECT ph FROM postHearts ph WHERE ph.user = :user AND ph.post = :post")
    Optional<PostHeart> findPostHeart(@Param("user") CustomUser user, @Param("post") Post post);
    List<PostHeart> findByUser(CustomUser user);
    List<PostHeart> findByPost(Post post);

}
