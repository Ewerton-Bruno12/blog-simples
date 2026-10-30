package com.ewerton.blog_simples.service;

import com.ewerton.blog_simples.dto.PostRequestDto;
import com.ewerton.blog_simples.dto.PostResponseDto;
import com.ewerton.blog_simples.exception.ResourceNotFoundException;
import com.ewerton.blog_simples.model.AuthorEntity;
import com.ewerton.blog_simples.model.CategoryEntity;
import com.ewerton.blog_simples.model.PostEntity;
import com.ewerton.blog_simples.model.TagEntity;
import com.ewerton.blog_simples.repository.AuthorRepository;
import com.ewerton.blog_simples.repository.CategoryRepository;
import com.ewerton.blog_simples.repository.PostRepository;
import com.ewerton.blog_simples.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado com o ID: " + id));
        return toDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto request) {
        AuthorEntity author = findAuthorOrThrow(request.authorId());
        CategoryEntity category = findCategoryOrThrow(request.categoryId());
        Set<TagEntity> tags = resolveTags(request.tagIds());

        PostEntity post = PostEntity.builder()
                .title(request.title())
                .content(request.content())
                .status(request.status())
                .author(author)
                .category(category)
                .tags(tags)
                .build();

        PostEntity postSaved = postRepository.save(post);

        return toDto(postSaved);
    }

    @Transactional
    public PostResponseDto update(Long id, PostRequestDto request) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado com o ID: " + id));

        AuthorEntity author = findAuthorOrThrow(request.authorId());
        CategoryEntity category = findCategoryOrThrow(request.categoryId());
        Set<TagEntity> tags = resolveTags(request.tagIds());

        post.setTitle(request.title());
        post.setContent(request.content());
        post.setStatus(request.status());
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(tags);

        return toDto(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Post não encontrado com o ID: " + id);
        }

        postRepository.deleteById(id);
    }

    private AuthorEntity findAuthorOrThrow(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o ID: " + authorId));
    }

    private CategoryEntity findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId));
    }

    private Set<TagEntity> resolveTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }

        List<TagEntity> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()) {
            Set<Long> foundIds = tags.stream()
                    .map(TagEntity::getId)
                    .collect(Collectors.toSet());

            Set<Long> missingIds = new HashSet<>(tagIds);
            missingIds.removeAll(foundIds);

            throw new ResourceNotFoundException("Tags não encontradas com os IDs: " + missingIds);
        }

        return new HashSet<>(tags);
    }

    private PostResponseDto toDto(PostEntity post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getAuthor().getName(),
                post.getCategory().getName(),
                post.getTags().stream()
                        .map(TagEntity::getName)
                        .collect(Collectors.toSet()),
                post.getCreatedAt()
        );
    }
}