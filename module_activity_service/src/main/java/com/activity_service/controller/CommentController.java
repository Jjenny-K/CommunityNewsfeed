package com.activity_service.controller;

import com.activity_service.domain.dto.CommentRequestDto;
import com.activity_service.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<?> comment(HttpServletRequest request,
                                     @PathVariable("postId") Long postId,
                                     @RequestBody CommentRequestDto commentRequestDto) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));
        CommentRequestDto comment = commentService.comment(userId, postId, commentRequestDto);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

}
