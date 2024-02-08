package com.preorderpurchase.repository;

import com.preorderpurchase.domain.entity.Comment;
import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(CustomUser user);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByPost(Post post);

}
