package com.ewerton.blog_simples.dto;

import com.ewerton.blog_simples.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PostRequestDto(
        @NotBlank(message = "O título do post é obrigatório")
        String title,
        @NotBlank(message = "O conteúdo do post é obrigatório")
        String content,
        @NotNull(message = "O status do post é obrigatório")
        PostStatus status,
        @NotNull(message = "O ID do autor é obrigatório")
        Long authorId,
        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoryId,
        Set<Long> tagIds
) {}
