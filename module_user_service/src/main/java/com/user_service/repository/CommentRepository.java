package com.user_service.repository;

import com.user_service.domain.entity.Comment;
import com.user_service.domain.entity.CustomUser;
import com.user_service.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(CustomUser user);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByPost(Post post);

}
