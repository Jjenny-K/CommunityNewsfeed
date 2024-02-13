package com.activity_service.service;

import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.entity.Comment;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.exception.ErrorCode;
//import com.activity_service.util.SecurityUtil;
import com.activity_service.domain.dto.CommentRequestDto;
import com.activity_service.domain.entity.Post;
import com.activity_service.exception.CustomApiException;
import com.activity_service.repository.CommentRepository;
import com.activity_service.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository,
                          CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // 댓글 작성
    @Transactional
    public CommentRequestDto comment(Long postId, CommentRequestDto commentRequestDto) {
//        CustomUser user = SecurityUtil.getCurrentUsername()
//                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
//                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_POST));

        /**
         * TODO : user_service, api gateway, ... - userId 값 연동 필요 (임의 사용자 지정)
         * userId = 1
         */

        Comment comment = Comment.builder()
                .userId(1)
                .post(post)
                .content(commentRequestDto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(1)
                .activityType(ActivityType.COMMENT)
                .activityId(savedComment.getId())
                .relatedUserId(post.getUserId())
                .build();

        /**
         * TODO : newsfeed_service - newsfeedCreateRequestDto create 연동 필요(임시 삭제)
         */

//        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return CommentRequestDto.from(savedComment);
    }

}
