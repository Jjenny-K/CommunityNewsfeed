package com.newsfeed_service.service;

import com.newsfeed_service.domain.entity.*;
import com.newsfeed_service.repository.*;
import com.newsfeed_service.util.SecurityUtil;
import com.newsfeed_service.domain.dto.NewsFeedResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CommentRepository commentRepository;
    private final PostHeartRepository postHeartRepository;
    private final CommentHeartRepository commentHeartRepository;

    public NewsFeedService(UserRepository userRepository,
                           FollowRepository followRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository,
                           PostHeartRepository postHeartRepository,
                           CommentHeartRepository commentHeartRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postHeartRepository = postHeartRepository;
        this.commentHeartRepository = commentHeartRepository;
    }

    // 팔로우 뉴스피드
    @Transactional(readOnly = true)
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

            NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
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

                NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
            }

            // 팔로잉한 사용자가 작성한 글
            List<Post> postList = postRepository.findByUser(followingUser);
            for (Post post : postList) {
                String message = followingUser.getName() + "님이 "
                        + post.getTitle() + " 포스트를 작성했습니다.";
                LocalDateTime createdAt = post.getCreatedAt();

                NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
            }

            // 팔로잉한 사용자가 작성한 댓글
            List<Comment> commentList = commentRepository.findByUser(followingUser);
            for (Comment comment : commentList) {
                String message = followingUser.getName() + "님이 "
                        + comment.getPost().getUser().getName() + "님의 글에 댓글을 남겼습니다.";
                LocalDateTime createdAt = comment.getCreatedAt();

                NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
            }

            // 팔로잉한 사용자가 좋아요한 글
            List<PostHeart> postHeartList = postHeartRepository.findByUser(followingUser);
            for (PostHeart postHeart : postHeartList) {
                String message = followingUser.getName() + "님이 "
                        + postHeart.getPost().getTitle() + " 포스트를 좋아합니다.";
                LocalDateTime createdAt = postHeart.getCreatedAt();

                NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
            }

            // 팔로잉한 사용자가 좋아요한 댓글
            List<CommentHeart> commentHeartList = commentHeartRepository.findByUser(followingUser);
            for (CommentHeart commentHerat : commentHeartList) {
                String message = followingUser.getName() + "님이 "
                        + commentHerat.getComment().getContent() + " 댓글을 좋아합니다.";
                LocalDateTime createdAt = commentHerat.getCreatedAt();

                NewsFeedFollowList.add(new NewsFeedResponseDto(message, createdAt));
            }
        }

        return NewsFeedFollowList.stream()
                .sorted(Comparator.comparing(NewsFeedResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 게시글 뉴스피드
    @Transactional(readOnly = true)
    public List<NewsFeedResponseDto> getNewsFeedPostList() {
        CustomUser user = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesWithProFileImageByEmail)
                .orElseThrow(() -> new BadCredentialsException("로그인 유저 정보가 없습니다."));

        List<NewsFeedResponseDto> NewsFeedPostList = new ArrayList<>();

        List<Post> postList = postRepository.findByUser(user);
        for (Post post : postList) {
            // 게시글에 작성된 댓글
            List<Comment> commentList = commentRepository.findByPost(post);
            for (Comment comment : commentList) {
                String message = comment.getUser().getName() + "님이 "
                        + post.getTitle() + " 포스트에 댓글을 남겼습니다.";
                LocalDateTime createdAt = comment.getCreatedAt();

                NewsFeedPostList.add(new NewsFeedResponseDto(message, createdAt));

                // 게시글 내 댓글 좋아요
                List<CommentHeart> commentHeartList = commentHeartRepository.findByComment(comment);
                for (CommentHeart commentHeart : commentHeartList) {
                    message = commentHeart.getUser().getName() + "님이 "
                            + comment.getContent() + " 댓글을 좋아합니다.";
                    createdAt = commentHeart.getCreatedAt();

                    NewsFeedPostList.add(new NewsFeedResponseDto(message, createdAt));
                }
            }

            // 게시글 좋아요
            List<PostHeart> postHeartList = postHeartRepository.findByPost(post);
            for (PostHeart postHeart : postHeartList) {
                String message = postHeart.getUser().getName() + "님이 "
                        + post.getTitle() + " 포스트를 좋아합니다.";
                LocalDateTime createdAt = postHeart.getCreatedAt();

                NewsFeedPostList.add(new NewsFeedResponseDto(message, createdAt));
            }
        }

        return NewsFeedPostList.stream()
                .sorted(Comparator.comparing(NewsFeedResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

}
