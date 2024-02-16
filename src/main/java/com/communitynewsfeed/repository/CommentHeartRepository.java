package com.communitynewsfeed.repository;

import com.communitynewsfeed.domain.entity.CommentHeart;
import com.communitynewsfeed.domain.entity.CustomUser;
import com.communitynewsfeed.domain.entity.Comment;
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
