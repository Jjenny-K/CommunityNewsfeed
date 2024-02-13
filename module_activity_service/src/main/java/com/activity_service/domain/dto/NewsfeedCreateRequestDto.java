package com.activity_service.domain.dto;

import com.activity_service.domain.type.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsfeedCreateRequestDto {

    private long userId;
    private ActivityType activityType;
    private long activityId;
    private long relatedUserId;

}
