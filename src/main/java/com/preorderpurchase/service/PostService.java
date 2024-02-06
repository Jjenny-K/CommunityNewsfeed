package com.preorderpurchase.service;

import com.preorderpurchase.domain.dto.PostRequestDto;
import com.preorderpurchase.domain.dto.PostResponseDto;
import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Post;
import com.preorderpurchase.repository.PostRepository;
import com.preorderpurchase.repository.UserRepository;
import com.preorderpurchase.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository,
                       PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 게시글 작성
    @Transactional
    public PostRequestDto post(PostRequestDto postRequestDto) {
        CustomUser postUser = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Post post = Post.builder()
                .postUser(postUser)
                .title(postRequestDto.getTitle())
                .description(postRequestDto.getDescription())
                .build();

        return postRequestDto.from(postRepository.save(post));
    }

    // 게시글 조회
    // 최신순으로 전부 조회
    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        for (Post post : postList) {
            postResponseDtoList.add(PostResponseDto.from(post));
        }

        return postResponseDtoList;
    }
}
