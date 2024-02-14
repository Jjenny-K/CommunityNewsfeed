package com.activity_service.controller;

import com.activity_service.domain.dto.PostResponseDto;
import com.activity_service.service.PostService;
import com.activity_service.domain.dto.PostRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> post(HttpServletRequest request,
                                  @Valid @RequestBody PostRequestDto postRequestDto) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));
        PostRequestDto post = postService.post(postRequestDto, userId);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    // 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList() {
        return ResponseEntity.ok().body(postService.getPostList());
    }

}
