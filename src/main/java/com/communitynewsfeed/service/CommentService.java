package com.communitynewsfeed.service;

import com.communitynewsfeed.domain.dto.CommentRequestDto;
import com.communitynewsfeed.domain.entity.CustomUser;
import com.communitynewsfeed.domain.entity.Post;
import com.communitynewsfeed.exception.CustomApiException;
import com.communitynewsfeed.exception.ErrorCode;
import com.communitynewsfeed.repository.CommentRepository;
import com.communitynewsfeed.repository.PostRepository;
import com.communitynewsfeed.repository.UserRepository;
import com.communitynewsfeed.util.SecurityUtil;
import com.communitynewsfeed.domain.entity.Comment;
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

    public CommentService(UserRepository userRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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

        return CommentRequestDto.from(commentRepository.save(comment));
    }

}
