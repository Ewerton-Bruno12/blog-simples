package com.ewerton.blog_simples.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDto(
        @NotBlank(message = "O nome do autor é obrigatório")
        String name,
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,
        String bio
) {}
