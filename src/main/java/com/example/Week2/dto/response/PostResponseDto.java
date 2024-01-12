package com.example.Week2.dto.response;

import com.example.Week2.entity.Comment;
import com.example.Week2.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        List<Comment> comments = post.getCommentList();
        comments.sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        //reversed()역순 //디폴트가 오름차순으로 .reversed()로 역순 적용
        for (Comment comment : comments) {
            this.commentList.add(new CommentResponseDto(comment));
        }
    }
}
