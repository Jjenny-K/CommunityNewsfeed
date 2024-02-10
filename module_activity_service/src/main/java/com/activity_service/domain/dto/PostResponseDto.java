package com.activity_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.activity_service.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private String title;

    private String postWriter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = InstantSerializer.class)
    private LocalDateTime createdAt;

    public static PostResponseDto from(Post post) {
        if (post == null) return null;

        return PostResponseDto.builder()
                .title(post.getTitle())
                .postWriter(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
