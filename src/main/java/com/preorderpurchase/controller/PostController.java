package com.preorderpurchase.controller;

import com.preorderpurchase.domain.dto.PostRequestDto;
import com.preorderpurchase.domain.dto.PostResponseDto;
import com.preorderpurchase.service.PostService;
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
    @PostMapping("/post")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> post(@RequestBody PostRequestDto postRequestDto) {
        PostRequestDto postDto = postService.post(postRequestDto);

        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    // 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList() {
        return ResponseEntity.ok().body(postService.getPostList());
    }

}
