package com.communitynewsfeed.domain.dto;

import com.communitynewsfeed.domain.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
