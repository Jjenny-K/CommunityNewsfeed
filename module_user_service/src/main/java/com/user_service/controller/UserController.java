package com.user_service.controller;

import com.user_service.domain.dto.UpdatePasswordDto;
import com.user_service.domain.dto.UpdateUserDto;
import com.user_service.domain.dto.UserResponseDto;
import com.user_service.service.ImageService;
import com.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    public UserController(UserService userService,
                          ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    // 본인 정보 조회
    @GetMapping("/users")
    public ResponseEntity<UserResponseDto> getMyUserInfo(HttpServletRequest request) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));

        return ResponseEntity.ok(userService.getMyUserInfo(userId));
    }

    // 본인 정보 수정
    @PutMapping("/users")
    public ResponseEntity<?> updateMyUserInfo(HttpServletRequest request,
                                              @Valid @RequestPart(name = "updateUserData") UpdateUserDto updateUserDto,
                                              @RequestPart(name = "profileImage") MultipartFile updateProfileImage)
            throws IOException {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));

        userService.updateMyUserInfo(updateUserDto, updateProfileImage, userId);

        UserResponseDto user = userService.getMyUserInfo(userId);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // 비밀번호 수정
    @PutMapping("/users/password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request,
                                            @Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        long userId = Long.parseLong(request.getHeader("X-USER-ID"));

        userService.updatePassword(updatePasswordDto, userId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "비밀번호 수정 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
