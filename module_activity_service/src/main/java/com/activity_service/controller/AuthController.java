package com.activity_service.controller;

import com.activity_service.domain.dto.*;
import com.activity_service.jwt.JwtFilter;
import com.activity_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestPart(name = "userData") UserRequestDto UserRequestDto,
                                    @RequestPart(name = "profileImage") MultipartFile profileImage)
            throws IOException {
        UserResponseDto user = authService.signup(UserRequestDto, profileImage);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", user.getName() + "-> 회원가입 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

}
