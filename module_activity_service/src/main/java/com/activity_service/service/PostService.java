package com.activity_service.service;

//import com.activity_service.util.SecurityUtil;
import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.dto.PostRequestDto;
import com.activity_service.domain.dto.PostResponseDto;
import com.activity_service.domain.entity.Post;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 작성
    @Transactional
    public PostRequestDto post(PostRequestDto postRequestDto) {
//        CustomUser user = SecurityUtil.getCurrentUsername()
//                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
//                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        /**
         * TODO : user_service, api gateway, ... - userId 값 연동 필요 (임의 사용자 지정)
         * userId = 1
         */

        Post post = Post.builder()
                .userId(1)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();

        Post savedPost = postRepository.save(post);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(1)
                .activityType(ActivityType.POST)
                .activityId(savedPost.getId())
                .build();

        /**
         * TODO : newsfeed_service - newsfeedCreateRequestDto create 연동 필요(임시 삭제)
         */

//        newsfeedService.createNewsfeed(newsfeedCreateRequestDto);

        return PostRequestDto.from(savedPost);
    }

    // 게시글 조회
    // 최신순으로 전부 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        // TODO : user_service - userName 연결 필요(임의 사용자 지정)

        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        for (Post post : postList) {
            postResponseDtoList.add(PostResponseDto.from(post, "test"));
        }

        return postResponseDtoList;
    }
}
