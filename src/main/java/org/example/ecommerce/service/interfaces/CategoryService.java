package org.example.ecommerce.service.interfaces;

import org.example.ecommerce.dto.request.CategoryRequestDto;
import org.example.ecommerce.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto dto);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);
    CategoryResponseDto getCategoryById(Long id);
    List<CategoryResponseDto> getAllCategories();
    void deleteCategory(Long id);
}