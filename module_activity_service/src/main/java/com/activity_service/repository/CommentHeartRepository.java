package com.activity_service.repository;

import com.activity_service.domain.entity.Comment;
import com.activity_service.domain.entity.CommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {

    @Query("SELECT ch FROM commentHearts ch WHERE ch.userId = :userId AND ch.comment = :comment")
    Optional<CommentHeart> findCommentHeart(@Param("userId") long userId, @Param("comment") Comment comment);
    List<CommentHeart> findByUserId(long userId);
    List<CommentHeart> findByComment(Comment comment);

}
