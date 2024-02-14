package com.activity_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userFeignClient", url = "localhost:8080/api/internal")
public interface UserFeignClient {
    @GetMapping("/users/findUserId/{userEmail}")
    long findUserId(@PathVariable("userEmail") String userEmail);

    @GetMapping("/users/findUserName/{userId}")
    String findUserName(@PathVariable("userId") String userId);
}
