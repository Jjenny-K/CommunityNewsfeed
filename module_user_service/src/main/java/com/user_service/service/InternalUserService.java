package com.user_service.service;

import com.user_service.domain.entity.CustomUser;
import com.user_service.exception.CustomApiException;
import com.user_service.exception.ErrorCode;
import com.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InternalUserService {

    private static final Logger logger = LoggerFactory.getLogger(InternalUserService.class);

    private final UserRepository userRepository;

    public InternalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long findUserId(String userEmail) {
        CustomUser user = userRepository.findOneWithAuthoritiesWithProFileImageByEmail(userEmail)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_USER));

        return user.getId();
    }

    public String findUserName(long userId) {
        CustomUser user = userRepository.findOneWithAuthoritiesWithProFileImageById(userId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_USER));

        return user.getName();
    }

}
