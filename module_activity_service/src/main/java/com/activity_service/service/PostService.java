package com.activity_service.service;

import com.activity_service.client.NewsfeedFeignClient;
import com.activity_service.client.UserFeignClient;
import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.dto.PostRequestDto;
import com.activity_service.domain.dto.PostResponseDto;
import com.activity_service.domain.entity.Post;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserFeignClient userFeignClient;
    private final NewsfeedFeignClient newsfeedFeignClient;

    public PostService(PostRepository postRepository,
                       UserFeignClient userFeignClient,
                       NewsfeedFeignClient newsfeedFeignClient) {
        this.postRepository = postRepository;
        this.userFeignClient = userFeignClient;
        this.newsfeedFeignClient = newsfeedFeignClient;
    }

    // 게시글 작성
    @Transactional
    public PostRequestDto post(PostRequestDto postRequestDto, long userId) {
        Post post = Post.builder()
                .userId(userId)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();

        Post savedPost = postRepository.save(post);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(userId)
                .activityType(ActivityType.POST)
                .activityId(savedPost.getId())
                .build();

        newsfeedFeignClient.createNewsfeed(newsfeedCreateRequestDto);

        return PostRequestDto.from(savedPost);
    }

    // 게시글 조회
    // 최신순으로 전부 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        for (Post post : postList) {
            String userName = userFeignClient.findUserName(String.valueOf(post.getUserId()));

            postResponseDtoList.add(PostResponseDto.from(post, userName));
        }

        return postResponseDtoList;
    }
}
