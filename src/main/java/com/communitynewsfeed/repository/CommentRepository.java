package com.communitynewsfeed.repository;

import com.communitynewsfeed.domain.entity.CustomUser;
import com.communitynewsfeed.domain.entity.Post;
import com.communitynewsfeed.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(CustomUser user);
    Optional<Comment> findById(Long commentId);
    List<Comment> findByPost(Post post);

}
