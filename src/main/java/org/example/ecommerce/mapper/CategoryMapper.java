package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.request.CategoryRequestDto;
import org.example.ecommerce.dto.response.CategoryResponseDto;
import org.example.ecommerce.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    ProductCategory toEntity(CategoryRequestDto dto);
    CategoryResponseDto toDto(ProductCategory category);
}
