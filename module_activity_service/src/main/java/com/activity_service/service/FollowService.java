package com.activity_service.service;

import com.activity_service.domain.entity.Follow;
import com.activity_service.exception.CustomApiException;
import com.activity_service.exception.ErrorCode;
import com.activity_service.repository.FollowRepository;
//import com.activity_service.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    // 팔로우
    @Transactional
    public void follow(String friendUserEmail) {
//        CustomUser followerUser = SecurityUtil.getCurrentUsername()
//                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
//                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

//        CustomUser followingUser =
//                userRepository.findOneWithAuthoritiesWithProFileImageByEmail(friendUserEmail)
//                        .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_USER));

        /*
         * TODO : user_service, api gateway, ... - userId 연동 필요 (임의 사용자 지정)
         * followerUserId = 1, followingUserId = 2
         */

        long followerUserId = 1;
        long followingUserId = 2;

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

        followRepository.save(follow);
    }

}
