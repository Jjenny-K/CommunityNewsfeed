package com.activity_service.domain.dto;

import com.activity_service.domain.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotNull
    private String content;

    public static CommentRequestDto from(Comment comment) {
        if (comment == null) return null;

        return CommentRequestDto.builder()
                .content(comment.getContent())
                .build();
    }

}
