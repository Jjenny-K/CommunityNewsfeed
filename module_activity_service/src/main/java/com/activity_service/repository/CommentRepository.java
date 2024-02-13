package com.activity_service.repository;

import com.activity_service.domain.entity.Comment;
import com.activity_service.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId(long userId);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByPost(Post post);

}
