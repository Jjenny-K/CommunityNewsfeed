package com.preorderpurchase.repository;

import com.preorderpurchase.domain.entity.Comment;
import com.preorderpurchase.domain.entity.CommentHeart;
import com.preorderpurchase.domain.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {

    @Query("SELECT ch FROM commentHearts ch WHERE ch.commentHeartUser = :commentHeartUser AND ch.comment = :comment")
    Optional<CommentHeart> findCommentHeart(@Param("commentHeartUser") CustomUser commentHeartUser, @Param("comment") Comment comment);

}
