package com.communitynewsfeed.domain.dto;

import com.communitynewsfeed.domain.entity.Post;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    private String content;

    public static PostRequestDto from(Post post) {
        if (post == null) return null;

        return PostRequestDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
