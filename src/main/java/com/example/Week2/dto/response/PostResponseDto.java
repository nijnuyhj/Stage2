package com.example.Week2.dto.response;

import com.example.Week2.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    public PostResponseDto(Long id, String title, String content, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
