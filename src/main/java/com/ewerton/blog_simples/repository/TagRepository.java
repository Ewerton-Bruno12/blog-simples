package com.ewerton.blog_simples.repository;

import com.ewerton.blog_simples.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    boolean existsByName(String name);
}
