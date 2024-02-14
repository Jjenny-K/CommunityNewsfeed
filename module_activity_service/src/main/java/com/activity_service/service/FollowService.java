package com.activity_service.service;

import com.activity_service.client.NewsfeedFeignClient;
import com.activity_service.client.UserFeignClient;
import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import com.activity_service.domain.entity.Follow;
import com.activity_service.domain.type.ActivityType;
import com.activity_service.exception.CustomApiException;
import com.activity_service.exception.ErrorCode;
import com.activity_service.repository.FollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    private final FollowRepository followRepository;
    private final UserFeignClient userFeignClient;
    private final NewsfeedFeignClient newsfeedFeignClient;

    public FollowService(FollowRepository followRepository,
                         UserFeignClient userFeignClient,
                         NewsfeedFeignClient newsfeedFeignClient) {
        this.followRepository = followRepository;
        this.userFeignClient = userFeignClient;
        this.newsfeedFeignClient = newsfeedFeignClient;
    }

    // 팔로우
    @Transactional
    public void follow(long followerUserId, String friendUserEmail) {
        long followingUserId = userFeignClient.findUserId(friendUserEmail);

        // 자기 자신 follow 방지
        if (followerUserId == followingUserId)
            throw new CustomApiException(ErrorCode.INVALID_REQUEST);
        // 중복 follow 방지
        if (followRepository.findFollow(followerUserId, followingUserId).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_FOLLOW);


        Follow follow = Follow.builder()
                .followerUserId(followerUserId)
                .followingUserId(followingUserId)
                .build();

        Follow savedFollow = followRepository.save(follow);

        NewsfeedCreateRequestDto newsfeedCreateRequestDto = NewsfeedCreateRequestDto.builder()
                .userId(followerUserId)
                .activityType(ActivityType.FOLLOW)
                .activityId(savedFollow.getId())
                .relatedUserId(followingUserId)
                .build();

        newsfeedFeignClient.createNewsfeed(newsfeedCreateRequestDto);
    }

}
