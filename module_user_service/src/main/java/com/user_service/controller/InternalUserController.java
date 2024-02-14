package com.user_service.controller;

import com.user_service.service.InternalUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users")
public class InternalUserController {

    private final InternalUserService internalUserService;

    public InternalUserController(InternalUserService internalUserService) {
        this.internalUserService = internalUserService;
    }

    @GetMapping("/findUserId/{userEmail}")
    public ResponseEntity<?> findUserId(@PathVariable("userEmail") String userEmail) {
        return ResponseEntity.ok().body(internalUserService.findUserId(userEmail));
    }

    @GetMapping("/findUserName/{userId}")
    public ResponseEntity<?> findUserName(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().body(internalUserService.findUserName(Long.parseLong(userId)));
    }

}
