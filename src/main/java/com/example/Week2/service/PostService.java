package com.example.Week2.service;

import com.example.Week2.dto.request.PostRequestDto;
import com.example.Week2.dto.response.PostResponseDto;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.Post;
import com.example.Week2.repository.MemberRepository;
import com.example.Week2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Member member){
        Post post = postRequestDto.toEntity(member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public List<Post> getPost(){
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

}
