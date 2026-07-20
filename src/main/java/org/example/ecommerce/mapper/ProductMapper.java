package org.example.ecommerce.mapper;
import org.example.ecommerce.dto.request.ProductRequestDto;
import org.example.ecommerce.dto.response.ProductResponseDto;
import org.example.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toDto(Product product);
}
