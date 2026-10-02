package com.ewerton.blog_simples.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,
        String description
) {}
