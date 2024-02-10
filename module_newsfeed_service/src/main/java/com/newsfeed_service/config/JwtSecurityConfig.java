package com.newsfeed_service.config;

import com.newsfeed_service.jwt.JwtFilter;
import com.newsfeed_service.jwt.TokenProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;

    public JwtSecurityConfig(TokenProvider tokenProvider, StringRedisTemplate redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
                new JwtFilter(tokenProvider, redisTemplate),
                UsernamePasswordAuthenticationFilter.class
        );
    }

}
