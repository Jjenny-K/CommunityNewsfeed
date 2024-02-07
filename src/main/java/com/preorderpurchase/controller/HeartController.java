package com.preorderpurchase.controller;

import com.preorderpurchase.domain.entity.CommentHeart;
import com.preorderpurchase.domain.entity.PostHeart;
import com.preorderpurchase.service.HeartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class HeartController {

    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    // 게시글 좋아요
    @PostMapping("/heart")
    public ResponseEntity<?> postHeart(@PathVariable("postId") Long postId) {
        PostHeart postHeart =heartService.postHeart(postId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", postHeart.getPost().getTitle() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/heart")
    public ResponseEntity<?> commentHeart(@PathVariable("commentId") Long commentId) {
        CommentHeart commentHeart = heartService.commentHeart(commentId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", commentHeart.getComment().getComment() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
