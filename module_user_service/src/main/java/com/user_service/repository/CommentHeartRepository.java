package com.user_service.repository;

import com.user_service.domain.entity.Comment;
import com.user_service.domain.entity.CommentHeart;
import com.user_service.domain.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {

    @Query("SELECT ch FROM commentHearts ch WHERE ch.user = :user AND ch.comment = :comment")
    Optional<CommentHeart> findCommentHeart(@Param("user") CustomUser user, @Param("comment") Comment comment);
    List<CommentHeart> findByUser(CustomUser user);
    List<CommentHeart> findByComment(Comment comment);

}
