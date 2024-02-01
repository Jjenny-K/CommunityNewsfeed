package com.handicraft.controller;

import com.handicraft.domain.dto.UpdatePasswordDto;
import com.handicraft.domain.dto.UserResponseDto;
import com.handicraft.domain.dto.UpdateUserDto;
import com.handicraft.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 본인 정보 조회
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserInfo());
    }

    // 본인 정보 수정
    @PutMapping("/user")
    public ResponseEntity<?> updateMyUserInfo(@Valid @ModelAttribute UpdateUserDto updateUserDto) throws IOException {
        userService.updateMyUserInfo(updateUserDto);

        UserResponseDto user = userService.getMyUserInfo();

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    // 비밀번호 수정
    @PutMapping("/user/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(updatePasswordDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "비밀번호 수정 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

}
