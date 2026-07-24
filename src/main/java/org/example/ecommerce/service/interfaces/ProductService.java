package org.example.ecommerce.service.interfaces;

import org.example.ecommerce.dto.request.ProductRequestDto;
import org.example.ecommerce.dto.response.ProductResponseDto;
import org.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);
    ProductResponseDto getProductById(Long id);
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    void deleteProduct(Long id);
    Product getProductEntityById(Long id);
    Page<ProductResponseDto> getProductsByCategoryId(Long id, Pageable pageable);
    Page<ProductResponseDto> getProductsByNameContaining(String name, Pageable pageable);

}
