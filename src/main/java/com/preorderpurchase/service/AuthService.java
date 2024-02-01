package com.preorderpurchase.service;

import com.preorderpurchase.domain.dto.*;
import com.preorderpurchase.domain.entity.Authority;
import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.RefreshToken;
import com.preorderpurchase.exception.CustomApiException;
import com.preorderpurchase.exception.ErrorCode;
import com.preorderpurchase.jwt.TokenProvider;
import com.preorderpurchase.repository.RefreshTokenRepository;
import com.preorderpurchase.repository.UserRepository;
import com.preorderpurchase.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ImageService imageService;

    public AuthService(AuthenticationManagerBuilder authenticationManagerBuilder,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider,
                       StringRedisTemplate redisTemplate,
                       UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       ImageService imageService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.imageService = imageService;
    }

    // 회원가입
    @Transactional
    public UserRequestDto signup(UserRequestDto userDto) throws IOException {

        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new CustomApiException(ErrorCode.DUPLICATED_USER_NAME);
        }

        Authority authority = Authority.builder().authorityName("ROLE_USER").build();


        CustomUser user = CustomUser.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .greeting(userDto.getGreeting())
                .isActivated(true)
                .authorities(Collections.singleton(authority))
                .build();

        UserRequestDto userRequestDto = UserRequestDto.from(userRepository.save(user));

        if (!Objects.requireNonNull(userDto.getProfileImage().getContentType()).startsWith("image")) {
            throw new RuntimeException("이미지 파일이 아닙니다.");
        }

        imageService.profileImageUpload(new ProfileImageUploadDto((userDto.getProfileImage())), user.getEmail());

        return userRequestDto;
    }

    // 로그인
    @Transactional
    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenDto;
    }

    // 토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new CustomApiException(ErrorCode.EXPIRED_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomApiException(ErrorCode.UNKNOWN_ERROR));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomApiException(ErrorCode.ACCESS_DENIED);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    // 로그아웃
    @Transactional
    public void logout(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(jwt, jwt);

        refreshTokenRepository.deleteByKey(String.valueOf(SecurityUtil.getCurrentUsername()))
                .orElseThrow(() -> new CustomApiException(ErrorCode.ACCESS_DENIED));
    }

}
