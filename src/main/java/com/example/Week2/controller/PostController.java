package com.example.Week2.controller;

import com.example.Week2.dto.request.PostRequestDto;
import com.example.Week2.dto.response.PostResponseDto;
import com.example.Week2.dto.response.ResponseMessage;
import com.example.Week2.entity.Post;
import com.example.Week2.security.UserDetailsImpl;
import com.example.Week2.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<ResponseMessage<PostResponseDto>> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody PostRequestDto postRequestDto){
        PostResponseDto response = postService.createPost(postRequestDto,userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("게시글 작성 성공",response), HttpStatus.CREATED);
    }

    @GetMapping("/post")
    public List<Post> getAllPost(){
        return postService.getPost();
    }
}
