package com.newsfeed_service.service;

import com.newsfeed_service.repository.FollowRepository;
import com.newsfeed_service.util.SecurityUtil;
import com.newsfeed_service.domain.entity.CustomUser;
import com.newsfeed_service.domain.entity.Follow;
import com.newsfeed_service.exception.CustomApiException;
import com.newsfeed_service.exception.ErrorCode;
import com.newsfeed_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowService(UserRepository userRepository,
                         FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    // 팔로우
    @Transactional
    public void follow(String friendUserEmail) {
        CustomUser followerUser = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        CustomUser followingUser =
                userRepository.findOneWithAuthoritiesWithProFileImageByEmail(friendUserEmail)
                        .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_USER));

        // 자기 자신 follow 방지
        if (followerUser == followingUser)
            throw new CustomApiException(ErrorCode.INVALID_REQUEST);
        // 중복 follow 방지
        if (followRepository.findFollow(followerUser, followingUser).isPresent())
            throw new CustomApiException(ErrorCode.DUPLICATED_FOLLOW);


        Follow follow = Follow.builder()
                .followerUser(followerUser)
                .followingUser(followingUser)
                .build();

        followRepository.save(follow);
    }

}
