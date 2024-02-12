package com.newsfeed_service.repository;

import com.newsfeed_service.domain.entity.Newsfeed;
import com.newsfeed_service.domain.type.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

    List<Newsfeed> findByRelatedUserIdAndActivityType(long relatedUserId, ActivityType activityType);
    List<Newsfeed> findByUserIdAndActivityType(long userId, ActivityType activityType);
    List<Newsfeed> findByUserId(long userId);

}
