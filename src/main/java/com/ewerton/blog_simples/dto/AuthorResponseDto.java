package com.ewerton.blog_simples.dto;

import java.time.LocalDateTime;

public record AuthorResponseDto(
        Long id,
        String name,
        String email,
        String bio,
        LocalDateTime createdAt
) {}
