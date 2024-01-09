package com.example.Week2.repository;

import com.example.Week2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostIdOrderByModifiedAtDesc(Long id);
}
