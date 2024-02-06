package com.preorderpurchase.service;

import com.preorderpurchase.domain.dto.NewsFeedResponseDto;
import com.preorderpurchase.domain.entity.CustomUser;
import com.preorderpurchase.domain.entity.Follow;
import com.preorderpurchase.domain.entity.Post;
import com.preorderpurchase.repository.FollowRepository;
import com.preorderpurchase.repository.PostRepository;
import com.preorderpurchase.repository.UserRepository;
import com.preorderpurchase.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsFeedService {

    private static final Logger logger = LoggerFactory.getLogger(NewsFeedService.class);

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    public NewsFeedService(UserRepository userRepository,
                           FollowRepository followRepository,
                           PostRepository postRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
    }

    // 팔로우 뉴스피드
    public List<NewsFeedResponseDto> getNewsFeedFollowList() {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        List<NewsFeedResponseDto> NewsFeedFollowList = new ArrayList<>();

        // 나를 팔로워한 사용자들
        List<Follow> followerList = followRepository.findByFollowingUser(user);
        for (Follow follower : followerList) {
            String message = follower.getFollowerUser().getName() + "님이 사용자님을 팔로우합니다.";
            LocalDateTime createdAt = follower.getCreatedAt();

            NewsFeedResponseDto responseDto = new NewsFeedResponseDto(message, createdAt);
            NewsFeedFollowList.add(responseDto);
        }

        // 내가 팔로잉한 사용자들의 최신 활동
        List<Follow> followingList = followRepository.findByFollowerUser(user);
        for (Follow following : followingList) {
            // 팔로잉한 사용자가 팔로워한 사용자들
            CustomUser followingUser = following.getFollowingUser();

            List<Follow> followingFollowerList = followRepository.findByFollowerUser(followingUser);
            for (Follow followingFollower : followingFollowerList) {
                String message = followingUser.getName() + "님이 "
                        + followingFollower.getFollowingUser().getName() + "님을 팔로우합니다.";
                LocalDateTime createdAt = followingFollower.getCreatedAt();

                NewsFeedResponseDto responseDto = new NewsFeedResponseDto(message, createdAt);
                NewsFeedFollowList.add(responseDto);
            }

            // 팔로잉한 사용자가 작성한 글
            List<Post> postList = postRepository.findByPostUser(followingUser);
            for (Post post : postList) {
                String message = followingUser.getName() + "님이 "
                        + post.getTitle() + " 포스트를 작성했습니다.";
                LocalDateTime createAt = post.getCreatedAt();

                NewsFeedResponseDto responseDto = new NewsFeedResponseDto(message, createAt);
                NewsFeedFollowList.add(responseDto);
            }
            // 팔로잉한 사용자가 작성한 댓글
            // 팔로잉한 사용자가 좋아요한 글
            // 팔로잉한 사용자가 좋아요한 댓글
        }

        return NewsFeedFollowList.stream()
                .sorted(Comparator.comparing(NewsFeedResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
