package org.example.ecommerce.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.request.CategoryRequestDto;
import org.example.ecommerce.dto.response.CategoryResponseDto;
import org.example.ecommerce.entity.ProductCategory;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.mapper.CategoryMapper;
import org.example.ecommerce.repository.ProductCategoryRepository;
import org.example.ecommerce.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ProductCategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        ProductCategory category = categoryMapper.toEntity(dto);
        ProductCategory savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(dto.getName());
        ProductCategory updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}