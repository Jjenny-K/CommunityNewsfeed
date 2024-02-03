package com.preorderpurchase.controller;

import com.preorderpurchase.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 팔로우
    @PostMapping("/follow/{friendUserEmail}")
    public ResponseEntity<?> follow(@PathVariable("friendUserEmail") String friendUserEmail) {
        followService.follow(friendUserEmail);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "팔로우 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
