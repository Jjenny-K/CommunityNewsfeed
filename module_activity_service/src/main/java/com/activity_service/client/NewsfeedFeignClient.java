package com.activity_service.client;

import com.activity_service.domain.dto.NewsfeedCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "newsfeedFeignClient", url = "localhost:8082/api/internal")
public interface NewsfeedFeignClient {
    @PostMapping("/newsfeeds")
    void createNewsfeed(NewsfeedCreateRequestDto newsfeedCreateRequestDto);
}
