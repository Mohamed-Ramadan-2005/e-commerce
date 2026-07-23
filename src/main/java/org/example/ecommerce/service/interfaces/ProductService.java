package org.example.ecommerce.service.interfaces;

import org.example.ecommerce.dto.request.ProductRequestDto;
import org.example.ecommerce.dto.response.ProductResponseDto;
import org.example.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllProducts();
    void deleteProduct(Long id);
    Product getProductEntityById(Long id);
    List<ProductResponseDto> getProductsByCategoryId(Long id);
}
