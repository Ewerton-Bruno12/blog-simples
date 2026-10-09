package com.ewerton.blog_simples.service;

import com.ewerton.blog_simples.dto.AuthorRequestDto;
import com.ewerton.blog_simples.dto.AuthorResponseDto;
import com.ewerton.blog_simples.exception.EmailAlreadyExistsException;
import com.ewerton.blog_simples.exception.ResourceNotFoundException;
import com.ewerton.blog_simples.model.AuthorEntity;
import com.ewerton.blog_simples.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorResponseDto findById(Long id) {
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o ID: " + id));
        return toResponseDto(author);
    }

    @Transactional
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        if (authorRepository.existsByEmail(authorRequestDto.email())) {
            throw new EmailAlreadyExistsException("O e-mail '" + authorRequestDto.email() + "' já está em uso por outro autor.");
        }

        AuthorEntity author = AuthorEntity.builder()
                .name(authorRequestDto.name())
                .email(authorRequestDto.email())
                .bio(authorRequestDto.bio())
                .build();

        AuthorEntity authorSaved = authorRepository.save(author);

        return toResponseDto(authorSaved);
    }

    @Transactional
    public AuthorResponseDto update(Long id, AuthorRequestDto request) {
        AuthorEntity author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o ID: " + id));

        if (!author.getEmail().equalsIgnoreCase(request.email())
                && authorRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("O e-mail '" + request.email() + "' já está em uso por outro autor.");
        }

        author.setName(request.name());
        author.setEmail(request.email());
        author.setBio(request.bio());

        AuthorEntity authorUpdated = authorRepository.save(author);

        return toResponseDto(authorUpdated);
    }

    @Transactional
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Autor não encontrado com o ID: " + id);
        }

        authorRepository.deleteById(id);
    }
    
    private AuthorResponseDto toResponseDto(AuthorEntity entity) {
        return new AuthorResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getBio(),
                entity.getCreatedAt()
        );
    }
}
