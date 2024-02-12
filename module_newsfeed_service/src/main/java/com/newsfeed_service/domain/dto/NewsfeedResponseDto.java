package com.newsfeed_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.newsfeed_service.domain.entity.Newsfeed;
import com.newsfeed_service.domain.type.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsfeedResponseDto {

    private long userId;

    private ActivityType activityType;

    private long activityId;

    private long relatedUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = InstantSerializer.class)
    private LocalDateTime createdAt;

    public static NewsfeedResponseDto from(Newsfeed newsfeed) {
        if (newsfeed == null) return null;

        return NewsfeedResponseDto.builder()
                .userId(newsfeed.getUserId())
                .activityType(newsfeed.getActivityType())
                .activityId(newsfeed.getActivityId())
                .relatedUserId(newsfeed.getRelatedUserId())
                .createdAt(newsfeed.getCreatedAt())
                .build();
    }

}
