package com.ewerton.blog_simples.service;

import com.ewerton.blog_simples.dto.CategoryRequestDto;
import com.ewerton.blog_simples.dto.CategoryResponseDto;
import com.ewerton.blog_simples.exception.NameAlreadyExistsException;
import com.ewerton.blog_simples.exception.ResourceNotFoundException;
import com.ewerton.blog_simples.model.CategoryEntity;
import com.ewerton.blog_simples.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
        return toDto(category);
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.existsByName(categoryRequestDto.name())) {
            throw new NameAlreadyExistsException("A categoria '" + categoryRequestDto.name() + "' já está cadastrada");
        }

        CategoryEntity category = CategoryEntity.builder()
                .name(categoryRequestDto.name())
                .description(categoryRequestDto.description())
                .build();

        CategoryEntity categorySaved = categoryRepository.save(category);

        return toDto(categorySaved);
    }

    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto request) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));

        if (!category.getName().equalsIgnoreCase(request.name())
                && categoryRepository.existsByName(request.name())) {
            throw new NameAlreadyExistsException("A categoria '" + request.name() + "' já está cadastrada por outro registro.");
        }

        category.setName(request.name());
        category.setDescription(request.description());

        CategoryEntity categoryUpdated = categoryRepository.save(category);

        return toDto(categoryUpdated);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Categoria não encontrada com o ID:" + id);
        }

        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto toDto(CategoryEntity entity) {
        return new CategoryResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
