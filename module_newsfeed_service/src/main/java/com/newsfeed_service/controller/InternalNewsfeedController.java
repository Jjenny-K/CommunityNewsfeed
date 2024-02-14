package com.newsfeed_service.controller;

import com.newsfeed_service.domain.dto.NewsfeedCreateRequestDto;
import com.newsfeed_service.service.NewsfeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/newsfeeds")
public class InternalNewsfeedController {

    private final NewsfeedService newsfeedService;

    public InternalNewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    @PostMapping()
    public ResponseEntity<?> createNewsfeed(@RequestBody NewsfeedCreateRequestDto newsfeedCreateRequestDto) {
        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return ResponseEntity.ok().build();
    }
}
