package com.communitynewsfeed.domain.dto;

import com.communitynewsfeed.domain.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import lombok.*;

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
