package com.activity_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
