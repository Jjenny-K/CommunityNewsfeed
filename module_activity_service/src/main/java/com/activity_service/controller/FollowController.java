package com.activity_service.controller;

import com.activity_service.service.FollowService;
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
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 팔로우
    @PostMapping("/follows/{friendUserEmail}")
    public ResponseEntity<?> follow(HttpServletRequest request,
                                    @PathVariable("friendUserEmail") String friendUserEmail) {
        long followerUserId = Long.parseLong(request.getHeader("X-USER-ID"));

        followService.follow(followerUserId, friendUserEmail);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "팔로우 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
