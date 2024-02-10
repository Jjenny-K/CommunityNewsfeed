package com.user_service.service;

import com.user_service.domain.dto.PostRequestDto;
import com.user_service.domain.dto.PostResponseDto;
import com.user_service.domain.entity.CustomUser;
import com.user_service.domain.entity.Post;
import com.user_service.repository.PostRepository;
import com.user_service.repository.UserRepository;
import com.user_service.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();

        return PostRequestDto.from(postRepository.save(post));
    }

    // 게시글 조회
    // 최신순으로 전부 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        for (Post post : postList) {
            postResponseDtoList.add(PostResponseDto.from(post));
        }

        return postResponseDtoList;
    }
}
