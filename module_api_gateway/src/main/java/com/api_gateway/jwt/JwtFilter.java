package com.api_gateway.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_INFO_HEADER = "X-USER-ID";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;

    public static class Config {}

    public JwtFilter(TokenProvider tokenProvider, StringRedisTemplate redisTemplate) {
        super(Config.class);

        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String requestURI = request.getURI().getPath();
            String accessToken = resolveToken(request);

            try {
                if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                    if (redisTemplate.opsForValue().get(accessToken) != null) {
                        throw new RuntimeException("토큰이 유효하지 않습니다.");
                    }

                    Long userId = tokenProvider.getUserId(accessToken);

                    request.mutate().header(USER_INFO_HEADER, String.valueOf(userId)).build();
                }
            } catch (Exception ex) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.setComplete();
                logger.error(ex.toString());
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            }

            return chain.filter(exchange);
        };
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().get(AUTHORIZATION_HEADER).get(0);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
