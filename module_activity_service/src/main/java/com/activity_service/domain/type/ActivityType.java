package com.activity_service.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {

    FOLLOW("follow"),
    POST("post"),
    COMMENT("comment"),
    POST_HEART("postHeart"),
    COMMENT_HEART("commentHeart"),;

    private final String activityType;

}
