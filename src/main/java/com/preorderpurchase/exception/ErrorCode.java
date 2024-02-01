package com.preorderpurchase.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, 1001, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED,1002, "변조된 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,1003, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,1004, "변조된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,1005, "권한이 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, 1007, "사용자를 찾을 수 없습니다."),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, 1008, "이미 가입된 사용자입니다."),;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

}
