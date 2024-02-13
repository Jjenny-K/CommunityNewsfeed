package com.preorderpurchase.controller;

import com.preorderpurchase.domain.dto.NewsfeedResponseDto;
import com.preorderpurchase.service.NewsfeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/newsfeeds")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    public NewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    // 팔로우 뉴스피드
    @GetMapping("/follows")
    public ResponseEntity<List<NewsfeedResponseDto>> getNewsfeedFollowList() {
        return ResponseEntity.ok().body(newsfeedService.getNewsfeedFollowList());
    }

    // 게시물 뉴스피드
    @GetMapping("/posts")
    public ResponseEntity<List<NewsfeedResponseDto>> getNewsfeedPostList() {
        return ResponseEntity.ok().body(newsfeedService.getNewsfeedPostList());
    }

}
