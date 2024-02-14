package com.activity_service.controller;

import com.activity_service.domain.entity.CommentHeart;
import com.activity_service.domain.entity.PostHeart;
import com.activity_service.service.HeartService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> postHeart(HttpServletRequest request,
                                       @PathVariable("postId") Long postId) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));
        PostHeart postHeart = heartService.postHeart(userId, postId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", postHeart.getPost().getTitle() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/hearts")
    public ResponseEntity<?> commentHeart(HttpServletRequest request,
                                          @PathVariable("commentId") Long commentId) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));
        CommentHeart commentHeart = heartService.commentHeart(userId, commentId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", commentHeart.getComment().getContent() + "-> 좋아요 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
