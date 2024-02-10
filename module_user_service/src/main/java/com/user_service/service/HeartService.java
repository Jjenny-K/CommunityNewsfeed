package com.user_service.service;

import com.user_service.domain.entity.*;
import com.user_service.exception.CustomApiException;
import com.user_service.exception.ErrorCode;
import com.user_service.repository.*;
import com.user_service.util.SecurityUtil;
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
    private final CommentRepository commentRepository;
    private final PostHeartRepository postHeartRepository;
    private final CommentHeartRepository commentHeartRepository;

    public HeartService(UserRepository userRepository,
                        PostRepository postRepository,
                        CommentRepository commentRepository,
                        PostHeartRepository postHeartRepository,
                        CommentHeartRepository commentHeartRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postHeartRepository = postHeartRepository;
        this.commentHeartRepository = commentHeartRepository;
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
                .user(user)
                .post(post)
                .build();

        return postHeartRepository.save(postHeart);
    }

    // 게시글 좋아요
    @Transactional
    public CommentHeart commentHeart(Long commentId) {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_COMMENT));

        // 중복 좋아요 방지
        if (commentHeartRepository.findCommentHeart(user, comment).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_COMMENT_HEART);

        CommentHeart commentHeart = CommentHeart.builder()
                .user(user)
                .comment(comment)
                .build();

        return commentHeartRepository.save(commentHeart);
    }

}
