package com.handicraft.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // 모든 예외를 처리하는 메소드
    // Bean 내에서 발생하는 예외를 처리
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 형식에 맞지 않는 요청이 입력되었을 때 처리하는 메소드
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> exceptionResponse = new HashMap<>();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd 'T' HH:mm:ss"));
        exceptionResponse.put("timestamp", timestamp);

        exceptionResponse.put("error", "BAD_REQUEST");

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + "는 " + e.getDefaultMessage())
                .collect(Collectors.toList());
        exceptionResponse.put("messages", errors);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // 사용자가 존재하지 않거나 권한이 없을 때 처리하는 메소드
    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<?> handleUserNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN, ex.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    // 사용자가 정보가 일치하지 않았을 때 처리하는 메소드
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<?> handleBadCredentialsException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // ErrorCode에 정의한 에러가 발생했을 때 처리하는 메소드
    @ExceptionHandler(CustomApiException.class)
    protected ResponseEntity<ExceptionResponse> handleCustomException(CustomApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return handleExceptionInternal(errorCode);
    };

    private ResponseEntity<ExceptionResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ExceptionResponse(errorCode));
    }

}
