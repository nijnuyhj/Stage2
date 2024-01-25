package com.example.Week2.dto.request;

import com.example.Week2.entity.Member;
import com.example.Week2.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;

    public Post toEntity(Member member){
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }


}
