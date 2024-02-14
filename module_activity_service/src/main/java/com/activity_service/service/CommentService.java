package com.activity_service.service;

import com.activity_service.client.NewsfeedFeignClient;
import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.entity.Comment;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.exception.ErrorCode;
import com.activity_service.domain.dto.CommentRequestDto;
import com.activity_service.domain.entity.Post;
import com.activity_service.exception.CustomApiException;
import com.activity_service.repository.CommentRepository;
import com.activity_service.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NewsfeedFeignClient newsfeedFeignClient;

    public CommentService(PostRepository postRepository,
                          CommentRepository commentRepository,
                          NewsfeedFeignClient newsfeedFeignClient) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.newsfeedFeignClient = newsfeedFeignClient;
    }

    // 댓글 작성
    @Transactional
    public CommentRequestDto comment(long userId, long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.builder()
                .userId(userId)
                .post(post)
                .content(commentRequestDto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(userId)
                .activityType(ActivityType.COMMENT)
                .activityId(savedComment.getId())
                .relatedUserId(post.getUserId())
                .build();

        newsfeedFeignClient.createNewsfeed(newsfeedCreateRequestDto);

        return CommentRequestDto.from(savedComment);
    }

}
