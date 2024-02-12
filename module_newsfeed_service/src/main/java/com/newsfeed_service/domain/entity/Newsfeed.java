package com.newsfeed_service.domain.entity;

import com.newsfeed_service.domain.core.BaseCreated;
import com.newsfeed_service.domain.type.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "newsfeeds")
@Table(name = "newsfeeds")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Newsfeed extends BaseCreated {

    @Id
    @Column(name = "newsfeedId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "userId", nullable = false)
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activityType", nullable = false)
    private ActivityType activityType;

    @Column(name = "activityId", nullable = false)
    private long activityId;

    @Column(name = "relatedUserId")
    private long relatedUserId;

}
