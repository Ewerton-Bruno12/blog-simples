package com.ewerton.blog_simples.service;

import com.ewerton.blog_simples.dto.TagRequestDto;
import com.ewerton.blog_simples.dto.TagResponseDto;
import com.ewerton.blog_simples.exception.NameAlreadyExistsException;
import com.ewerton.blog_simples.exception.ResourceNotFoundException;
import com.ewerton.blog_simples.exception.TagInUseException;
import com.ewerton.blog_simples.model.TagEntity;
import com.ewerton.blog_simples.repository.PostRepository;
import com.ewerton.blog_simples.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<TagResponseDto> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagResponseDto findById(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag não encontrada com o ID: " + id));
        return toDto(tag);
    }

    @Transactional
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        if (tagRepository.existsByName(tagRequestDto.name())) {
            throw new NameAlreadyExistsException("A tag '" + tagRequestDto.name() + "' já está cadastrada");
        }

        TagEntity tag = TagEntity.builder()
                .name(tagRequestDto.name())
                .build();

        TagEntity tagSaved = tagRepository.save(tag);

        return toDto(tagSaved);
    }

    @Transactional
    public TagResponseDto update(Long id, TagRequestDto request) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag não encontrada com o ID: " + id));

        if (!tag.getName().equalsIgnoreCase(request.name())
                && tagRepository.existsByName(request.name())) {
            throw new NameAlreadyExistsException("A tag '" + request.name() + "' já existe.");
        }

        tag.setName(request.name());
        return toDto(tag);
    }

    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Tag não encontrada com o ID: " + id);
        }

        if (postRepository.existsByTagsId(id)) {
            throw new TagInUseException("Não é possível deletar a tag porque ela está vinculada a posts");
        }

        tagRepository.deleteById(id);
    }

    private TagResponseDto toDto(TagEntity entity) {
        return new TagResponseDto(
                entity.getId(),
                entity.getName()
        );
    }
}
