package com.newsfeed_service.controller;

import com.newsfeed_service.domain.dto.NewsfeedResponseDto;
import com.newsfeed_service.service.NewsfeedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/newsfeeds")
public class NewsFeedController {

    private final NewsfeedService newsfeedService;

    public NewsFeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    // 팔로우 뉴스피드
    @GetMapping("/follows")
    public ResponseEntity<List<NewsfeedResponseDto>> getNewsfeedFollowList(HttpServletRequest request) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));

        return ResponseEntity.ok().body(newsfeedService.getNewsfeedFollowList(userId));
    }

    // 게시물 뉴스피드
    @GetMapping("/posts")
    public ResponseEntity<List<NewsfeedResponseDto>> getNewsfeedPostList(HttpServletRequest request) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));

        return ResponseEntity.ok().body(newsfeedService.getNewsfeedPostList(userId));
    }

}
