package com.activity_service.service;

import com.activity_service.client.NewsfeedFeignClient;
import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.entity.*;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.exception.CustomApiException;
import com.activity_service.exception.ErrorCode;
import com.activity_service.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeartService {

    private static final Logger logger = LoggerFactory.getLogger((HeartService.class));

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostHeartRepository postHeartRepository;
    private final CommentHeartRepository commentHeartRepository;
    private final NewsfeedFeignClient newsfeedFeignClient;

    public HeartService(PostRepository postRepository,
                        CommentRepository commentRepository,
                        PostHeartRepository postHeartRepository,
                        CommentHeartRepository commentHeartRepository,
                        NewsfeedFeignClient newsfeedFeignClient) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postHeartRepository = postHeartRepository;
        this.commentHeartRepository = commentHeartRepository;
        this.newsfeedFeignClient = newsfeedFeignClient;
    }

    // 게시글 좋아요
    @Transactional
    public PostHeart postHeart(long userId, long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_POST));

        // 중복 좋아요 방지
        if (postHeartRepository.findPostHeart(userId, post).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_POST_HEART);

        PostHeart postHeart = PostHeart.builder()
                .userId(userId)
                .post(post)
                .build();

        PostHeart savedPostHeart = postHeartRepository.save(postHeart);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(userId)
                .activityType(ActivityType.POST_HEART)
                .activityId(savedPostHeart.getId())
                .relatedUserId(post.getUserId())
                .build();

        newsfeedFeignClient.createNewsfeed(newsfeedCreateRequestDto);

        return savedPostHeart;
    }

    // 댓글 좋아요
    @Transactional
    public CommentHeart commentHeart(long userId, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_COMMENT));

        // 중복 좋아요 방지
        if (commentHeartRepository.findCommentHeart(userId, comment).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_COMMENT_HEART);

        CommentHeart commentHeart = CommentHeart.builder()
                .userId(userId)
                .comment(comment)
                .build();

        CommentHeart savedCommentHeart = commentHeartRepository.save(commentHeart);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(userId)
                .activityType(ActivityType.COMMENT_HEART)
                .activityId(savedCommentHeart.getId())
                .relatedUserId(comment.getUserId())
                .build();

        newsfeedFeignClient.createNewsfeed(newsfeedCreateRequestDto);

        return savedCommentHeart;
    }

}
