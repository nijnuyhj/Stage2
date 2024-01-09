package com.example.Week2.service;

import com.example.Week2.dto.request.CommentRequestDto;
import com.example.Week2.dto.response.CommentResponseDto;
import com.example.Week2.entity.Comment;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.Post;
import com.example.Week2.repository.CommentRepository;
import com.example.Week2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Member member){
        Post post = postIdValid(postId);
        Comment comment = new Comment(member, post, requestDto.getContent());
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }


    public Post postIdValid(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return post;
    }
}
