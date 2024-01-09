package com.example.Week2.controller;

import com.example.Week2.dto.request.CommentRequestDto;
import com.example.Week2.dto.response.CommentResponseDto;
import com.example.Week2.security.UserDetailsImpl;
import com.example.Week2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(postId,commentRequestDto,userDetails.getMember());
    }

}
