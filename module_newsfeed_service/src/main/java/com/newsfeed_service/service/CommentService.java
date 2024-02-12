package com.newsfeed_service.service;

import com.newsfeed_service.domain.dto.CommentRequestDto;
import com.newsfeed_service.domain.dto.NewsfeedCreateRequestDto;
import com.newsfeed_service.domain.entity.Comment;
import com.newsfeed_service.domain.type.ActivityType;
import com.newsfeed_service.util.SecurityUtil;
import com.newsfeed_service.domain.entity.CustomUser;
import com.newsfeed_service.domain.entity.Post;
import com.newsfeed_service.exception.CustomApiException;
import com.newsfeed_service.exception.ErrorCode;
import com.newsfeed_service.repository.CommentRepository;
import com.newsfeed_service.repository.PostRepository;
import com.newsfeed_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NewsfeedService newsfeedService;

    public CommentService(UserRepository userRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository,
                          NewsfeedService newsfeedService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.newsfeedService = newsfeedService;
    }

    // 댓글 작성
    @Transactional
    public CommentRequestDto comment(Long postId, CommentRequestDto commentRequestDto) {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentRequestDto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(user.getId())
                .activityType(ActivityType.COMMENT)
                .activityId(savedComment.getId())
                .relatedUserId(post.getUser().getId())
                .build();

        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return CommentRequestDto.from(savedComment);
    }

}
