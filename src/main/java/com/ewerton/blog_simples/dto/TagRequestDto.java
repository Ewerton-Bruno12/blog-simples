package com.ewerton.blog_simples.dto;

import jakarta.validation.constraints.NotBlank;

public record TagRequestDto(
        @NotBlank(message = "O nome da tag é obrigatório")
        String name
) {}
