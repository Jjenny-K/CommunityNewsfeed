package com.preorderpurchase.service;

import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Post;
import com.preorderpurchase.domain.entity.PostHeart;
import com.preorderpurchase.exception.CustomApiException;
import com.preorderpurchase.exception.ErrorCode;
import com.preorderpurchase.repository.PostHeartRepository;
import com.preorderpurchase.repository.PostRepository;
import com.preorderpurchase.repository.UserRepository;
import com.preorderpurchase.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeartService {

    private static final Logger logger = LoggerFactory.getLogger((HeartService.class));

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;

    public HeartService(UserRepository userRepository,
                        PostRepository postRepository,
                        PostHeartRepository postHeartRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postHeartRepository = postHeartRepository;
    }

    // 게시글 좋아요
    @Transactional
    public PostHeart postHeart(Long postId) {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_POST));

        // 중복 좋아요 방지
        if (postHeartRepository.findPostHeart(user, post).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_POST_HEART);

        PostHeart postHeart = PostHeart.builder()
                .postHeartUser(user)
                .post(post)
                .build();

        return postHeartRepository.save(postHeart);
    }
}
