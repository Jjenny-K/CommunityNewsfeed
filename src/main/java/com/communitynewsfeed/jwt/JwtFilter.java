package com.communitynewsfeed.jwt;

import com.communitynewsfeed.exception.CustomApiException;
import com.communitynewsfeed.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;

    public JwtFilter(TokenProvider tokenProvider, StringRedisTemplate redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 FilterChain filterChain)
            throws IOException, ServletException {
        String requestURI = httpServletRequest.getRequestURI();
        String jwt = resolveToken(httpServletRequest);

        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                if (redisTemplate.opsForValue().get(jwt) != null) {
                    throw new CustomApiException(ErrorCode.UNKNOWN_ERROR);
                }

                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
            logger.info("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            httpServletRequest.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (Exception ex) {
            httpServletRequest.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getCode());
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
