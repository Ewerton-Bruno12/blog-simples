package com.ewerton.blog_simples.repository;

import com.ewerton.blog_simples.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByTitleContainingIgnoreCase(String title);
}
