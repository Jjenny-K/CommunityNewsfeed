package com.newsfeed_service.service;

import com.newsfeed_service.domain.dto.NewsfeedCreateRequestDto;
import com.newsfeed_service.domain.dto.PostRequestDto;
import com.newsfeed_service.domain.entity.Post;
import com.newsfeed_service.domain.type.ActivityType;
import com.newsfeed_service.util.SecurityUtil;
import com.newsfeed_service.domain.dto.PostResponseDto;
import com.newsfeed_service.domain.entity.CustomUser;
import com.newsfeed_service.repository.PostRepository;
import com.newsfeed_service.repository.UserRepository;
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
    private final NewsfeedService newsfeedService;

    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       NewsfeedService newsFeedService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.newsfeedService = newsFeedService;
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

        Post savedPost = postRepository.save(post);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(user.getId())
                .activityType(ActivityType.POST)
                .activityId(savedPost.getId())
                .build();

        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return PostRequestDto.from(savedPost);
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
