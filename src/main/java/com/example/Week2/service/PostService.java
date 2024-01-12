package com.example.Week2.service;

import com.example.Week2.dto.request.PostRequestDto;
import com.example.Week2.dto.response.CommentResponseDto;
import com.example.Week2.dto.response.PostResponseDto;
import com.example.Week2.dto.response.ResponseMessage;
import com.example.Week2.entity.Comment;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.Post;
import com.example.Week2.repository.CommentRepository;
import com.example.Week2.repository.MemberRepository;
import com.example.Week2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@Service
@RequiredArgsConstructor

public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Member member){
        Post post = new Post(postRequestDto,member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public List<PostResponseDto> getAllPost(){
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDto = new ArrayList<>();

        for (Post post : posts) {
            postResponseDto.add(new PostResponseDto(post));
        }
        return postResponseDto;
    }

    @Transactional    // 글 상세보기
    public PostResponseDto getPost(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByModifiedAtDesc(postId); //수정된 시간 기준 내림차순 정렬
        List<CommentResponseDto> commentResponseDto = new ArrayList<>();
        for (Comment comment : commentList){
            commentResponseDto.add(new CommentResponseDto(comment));
        }
        return new PostResponseDto(postIdValid(postId));
    }

    @Transactional //변경감지
    public PostResponseDto changePost(Long postId, PostRequestDto requestDto, Member member) {
            Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 글이 존재하지 않습니다")
        );
            Member existUser = checkMember(member);
            checkSameMember(existUser,post.getMember());
                System.out.println(member.getId());
                System.out.println(post.getId());
                post.update(requestDto);
                return new PostResponseDto(post);
    }

    @Transactional
    public ResponseEntity<ResponseMessage> deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당 글이 존재하지 않습니다")
        );
        Member existUser = checkMember(member);
        checkSameMember(existUser,post.getMember());
        postRepository.delete(post);
        ResponseMessage responseMessage = new ResponseMessage("글이 성공적으로 삭제되었습니다.",null);
        return ResponseEntity.ok(responseMessage);
    }

    private Member checkMember(Member member) {
        return memberRepository.findByUsername(member.getUsername()).
                orElseThrow(() -> new IllegalArgumentException("권한이 없습니다"));

    }

    public void checkSameMember(Member existMember, Member member) {
        if (!existMember.getUsername().equals(member.getUsername())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    //게시글 유무 확인
    public Post postIdValid(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return post;
    }


}
