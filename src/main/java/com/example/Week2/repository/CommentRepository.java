package com.example.Week2.repository;

import com.example.Week2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostIdOrderByModifiedAtDesc(Long postId);
    Optional<Comment> findByIdAndMemberId(Long postId, Long memberId);
}
