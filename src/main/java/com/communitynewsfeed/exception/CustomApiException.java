package com.communitynewsfeed.exception;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CustomApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
