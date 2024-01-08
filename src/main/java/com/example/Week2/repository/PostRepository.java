package com.example.Week2.repository;

import com.example.Week2.dto.response.PostResponseDto;
import com.example.Week2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findById(Long postId);
}
