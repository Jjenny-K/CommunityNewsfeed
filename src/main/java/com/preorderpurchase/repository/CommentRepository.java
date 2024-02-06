package com.preorderpurchase.repository;

import com.preorderpurchase.domain.entity.Comment;
import com.preorderpurchase.domain.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCommentUser(CustomUser user);

}
