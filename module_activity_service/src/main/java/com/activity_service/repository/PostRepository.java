package com.activity_service.repository;

import com.activity_service.domain.entity.CustomUser;
import com.activity_service.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUser(CustomUser user);
    Optional<Post> findById(Long postId);

}
