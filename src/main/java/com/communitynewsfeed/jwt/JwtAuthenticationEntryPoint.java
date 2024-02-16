package com.communitynewsfeed.jwt;

import com.communitynewsfeed.exception.ErrorCode;
import com.communitynewsfeed.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        Integer exception = (Integer)request.getAttribute("exception");

        if(exception == null) {
            setErrorResponse(response, ErrorCode.UNKNOWN_ERROR);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception == 1002) {
            setErrorResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
        }
        //토큰 만료된 경우
        else if(exception == 1003) {
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception == 1004) {
            setErrorResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
        }
        else {
            setErrorResponse(response, ErrorCode.ACCESS_DENIED);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(errorCode.getHttpStatus().value());

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(LocalDateTime.now(), errorCode.getHttpStatus(), errorCode.getMessage());

        try{
            response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
