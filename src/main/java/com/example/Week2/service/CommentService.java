package com.example.Week2.service;

import com.example.Week2.dto.request.CommentRequestDto;
import com.example.Week2.dto.response.CommentResponseDto;
import com.example.Week2.dto.response.PostResponseDto;
import com.example.Week2.entity.Comment;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.Post;
import com.example.Week2.repository.CommentRepository;
import com.example.Week2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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

    @Transactional
    public CommentResponseDto updateComment(Long postId, CommentRequestDto commentRequestDto, Member member){
        Comment comment = commentIdByUserValid(postId, member);
        comment.update(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    public Post postIdValid(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return post;
    }
//
//    public Comment commentIdValid(Long id) {
//        Comment comment = commentRepository.findById(id).orElseThrow(
//                () -> new NullPointerException("본인이 작성한 댓글이 아닙니다")
//        );
//        return comment;
//    }
//
    public Comment commentIdByUserValid(Long postId, Member member) {
        Comment comment = commentRepository.findByIdAndMemberId(postId, member.getId()).orElseThrow(
                () -> new NullPointerException("본인이 작성한 글이 아닙니다")
        );
        return comment;
    }


}
