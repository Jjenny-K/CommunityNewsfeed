package com.handicraft.controller;

import com.handicraft.domain.dto.UserRequestDto;
import com.handicraft.domain.dto.UserResponseDto;
import com.handicraft.domain.entity.CustomUser;
import com.handicraft.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserRequestDto> signup(@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    // 본인 정보 조회
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserResponseDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyInfo());
    }

}
