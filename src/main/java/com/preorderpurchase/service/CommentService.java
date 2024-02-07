package com.preorderpurchase.service;

import com.preorderpurchase.domain.dto.CommentRequestDto;
import com.preorderpurchase.domain.entity.Comment;
import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Post;
import com.preorderpurchase.exception.CustomApiException;
import com.preorderpurchase.exception.ErrorCode;
import com.preorderpurchase.repository.CommentRepository;
import com.preorderpurchase.repository.PostRepository;
import com.preorderpurchase.repository.UserRepository;
import com.preorderpurchase.util.SecurityUtil;
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
