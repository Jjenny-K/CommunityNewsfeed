package com.activity_service.controller;

import com.activity_service.domain.entity.CommentHeart;
import com.activity_service.domain.entity.PostHeart;
import com.activity_service.service.HeartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}")
public class HeartController {

    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    // 게시글 좋아요
    @PostMapping("/hearts")
    public ResponseEntity<?> postHeart(@PathVariable("postId") Long postId) {
        PostHeart postHeart = heartService.postHeart(postId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", postHeart.getPost().getTitle() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/hearts")
    public ResponseEntity<?> commentHeart(@PathVariable("commentId") Long commentId) {
        CommentHeart commentHeart = heartService.commentHeart(commentId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", commentHeart.getComment().getContent() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
