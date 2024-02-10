package com.activity_service.controller;

import com.activity_service.domain.dto.CommentRequestDto;
import com.activity_service.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<?> comment(@PathVariable("postId") Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentRequestDto comment = commentService.comment(postId, commentRequestDto);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

}