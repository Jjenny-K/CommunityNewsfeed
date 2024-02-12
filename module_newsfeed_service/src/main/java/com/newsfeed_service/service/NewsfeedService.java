package com.newsfeed_service.service;

import com.newsfeed_service.domain.dto.NewsfeedCreateRequestDto;
import com.newsfeed_service.domain.dto.NewsfeedResponseDto;
import com.newsfeed_service.domain.entity.CustomUser;
import com.newsfeed_service.domain.entity.Newsfeed;
import com.newsfeed_service.domain.type.ActivityType;
import com.newsfeed_service.repository.NewsfeedRepository;
import com.newsfeed_service.repository.UserRepository;
import com.newsfeed_service.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsfeedService {

    private static final Logger logger = LoggerFactory.getLogger(NewsfeedService.class);

    private final UserRepository userRepository;
    private final NewsfeedRepository newsfeedRepository;

    public NewsfeedService(UserRepository userRepository,
                           NewsfeedRepository newsfeedRepository) {
        this.userRepository = userRepository;
        this.newsfeedRepository = newsfeedRepository;
    }

    // 뉴스피드 생성
    @Transactional
    public void createNewsfeed(NewsfeedCreateRequestDto newsfeedCreateRequestDto) {
        Newsfeed newsfeed = Newsfeed.builder()
                .userId(newsfeedCreateRequestDto.getUserId())
                .activityType(newsfeedCreateRequestDto.getActivityType())
                .activityId(newsfeedCreateRequestDto.getActivityId())
                .relatedUserId(newsfeedCreateRequestDto.getRelatedUserId())
                .build();

        newsfeedRepository.save(newsfeed);
    }

    // 팔로우 뉴스피드
    @Transactional(readOnly = true)
    public List<NewsfeedResponseDto> getNewsfeedFollowList() {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        List<Newsfeed> newsfeedFollowList = new ArrayList<>();

        // 사용자가 팔로잉하는 사용자들의 활동
        List<Newsfeed> followingList =
                newsfeedRepository.findByUserIdAndActivityType(user.getId(), ActivityType.FOLLOW);

        for (Newsfeed following : followingList) {
            newsfeedFollowList.addAll(newsfeedRepository.findByUserId(following.getRelatedUserId()));
        }

        // 사용자를 팔로잉하는 사용자
        List<Newsfeed> followerList =
                newsfeedRepository.findByRelatedUserIdAndActivityType(user.getId(), ActivityType.FOLLOW);

        newsfeedFollowList.addAll(followerList);

        return newsfeedFollowList.stream()
                .map(NewsfeedResponseDto::from)
                .sorted(Comparator.comparing(NewsfeedResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 게시글 뉴스피드
    @Transactional(readOnly = true)
    public List<NewsfeedResponseDto> getNewsfeedPostList() {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        // 사용자가 작성한 게시글 내 활동
        List<ActivityType> activityTypes =
                new ArrayList<>(
                        Arrays.asList(ActivityType.COMMENT, ActivityType.POST_HEART, ActivityType.COMMENT_HEART));

        List<Newsfeed> newsfeedPostList =
                newsfeedRepository.findByRelatedUserIdAndActivityTypes(user.getId(), activityTypes);

        return newsfeedPostList.stream()
                .map(NewsfeedResponseDto::from)
                .sorted(Comparator.comparing(NewsfeedResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

}
