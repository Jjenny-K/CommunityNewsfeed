package com.activity_service.controller;

import com.activity_service.service.NewsFeedService;
import com.activity_service.domain.dto.NewsFeedResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/newsfeeds")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    public NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    // 팔로우 뉴스피드
    @GetMapping("/follows")
    public ResponseEntity<List<NewsFeedResponseDto>> getFollowingList() {
        return ResponseEntity.ok().body(newsFeedService.getNewsFeedFollowList());
    }

    // 게시물 뉴스피드
    @GetMapping("/posts")
    public ResponseEntity<List<NewsFeedResponseDto>> getPostList() {
        return ResponseEntity.ok().body(newsFeedService.getNewsFeedPostList());
    }

}
