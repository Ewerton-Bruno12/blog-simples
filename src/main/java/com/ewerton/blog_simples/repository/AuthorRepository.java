package com.ewerton.blog_simples.repository;

import com.ewerton.blog_simples.model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByEmail(String email);
}
