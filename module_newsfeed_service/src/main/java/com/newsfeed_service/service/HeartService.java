package com.newsfeed_service.service;

import com.newsfeed_service.domain.dto.NewsfeedCreateRequestDto;
import com.newsfeed_service.domain.entity.*;
import com.newsfeed_service.domain.type.ActivityType;
import com.newsfeed_service.repository.*;
import com.newsfeed_service.util.SecurityUtil;
import com.newsfeed_service.exception.CustomApiException;
import com.newsfeed_service.exception.ErrorCode;
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
    private final NewsfeedService newsfeedService;

    public HeartService(UserRepository userRepository,
                        PostRepository postRepository,
                        CommentRepository commentRepository,
                        PostHeartRepository postHeartRepository,
                        CommentHeartRepository commentHeartRepository,
                        NewsfeedService newsfeedService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postHeartRepository = postHeartRepository;
        this.commentHeartRepository = commentHeartRepository;
        this.newsfeedService = newsfeedService;
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

        PostHeart savedPostHeart = postHeartRepository.save(postHeart);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(user.getId())
                .activityType(ActivityType.POST_HEART)
                .activityId(savedPostHeart.getId())
                .relatedUserId(post.getUser().getId())
                .build();

        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return savedPostHeart;
    }

    // 댓글 좋아요
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

        CommentHeart savedCommentHeart = commentHeartRepository.save(commentHeart);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(user.getId())
                .activityType(ActivityType.POST_HEART)
                .activityId(savedCommentHeart.getId())
                .relatedUserId(comment.getUser().getId())
                .build();

        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return savedCommentHeart;
    }

}
