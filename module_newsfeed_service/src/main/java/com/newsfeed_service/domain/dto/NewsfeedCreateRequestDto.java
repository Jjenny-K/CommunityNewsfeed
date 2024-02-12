package com.newsfeed_service.domain.dto;

import com.newsfeed_service.domain.type.ActivityType;
import lombok.*;

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
