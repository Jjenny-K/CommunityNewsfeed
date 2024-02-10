package com.newsfeed_service.controller;

import com.newsfeed_service.domain.dto.PostRequestDto;
import com.newsfeed_service.domain.dto.PostResponseDto;
import com.newsfeed_service.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping("/posts")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> post(@Valid @RequestBody PostRequestDto postRequestDto) {
        PostRequestDto post = postService.post(postRequestDto);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    // 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList() {
        return ResponseEntity.ok().body(postService.getPostList());
    }

}
