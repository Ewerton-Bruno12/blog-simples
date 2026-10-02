package com.ewerton.blog_simples.dto;

import com.ewerton.blog_simples.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        PostStatus status,
        String authorName,
        String categoryName,
        Set<String> tagNames,
        LocalDateTime createdAt
) {}
