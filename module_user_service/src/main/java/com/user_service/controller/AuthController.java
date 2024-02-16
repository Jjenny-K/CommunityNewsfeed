package com.user_service.controller;

//import com.user_service.jwt.JwtFilter;
import com.user_service.service.AuthService;
import com.user_service.domain.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
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
        httpHeaders.add(AUTHORIZATION_HEADER, BEARER_PREFIX + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

//    // 토큰 재발급
//    @PostMapping("/reissue")
//    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
//        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
//    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 성공");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

}
