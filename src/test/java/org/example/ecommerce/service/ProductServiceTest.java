package org.example.ecommerce.service;

import org.example.ecommerce.dto.request.ProductRequestDto;
import org.example.ecommerce.dto.response.ProductResponseDto;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.mapper.ProductMapper;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product mockProduct;
    private ProductRequestDto requestDto;
    private ProductResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Gaming Mouse");
        mockProduct.setPrice(50.0);
        mockProduct.setStockQuantity(100);

        requestDto = new ProductRequestDto();
        requestDto.setName("Gaming Mouse");
        requestDto.setPrice(50.0);
        requestDto.setStockQuantity(100);

        responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Gaming Mouse");
        responseDto.setPrice(50.0);
        responseDto.setStockQuantity(100);
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productMapper.toEntity(any(ProductRequestDto.class))).thenReturn(mockProduct);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(responseDto);

        ProductResponseDto response = productService.createProduct(requestDto);

        assertNotNull(response);
        assertEquals("Gaming Mouse", response.getName());
        assertEquals(50.0, response.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productMapper.toDto(any(Product.class))).thenReturn(responseDto);

        ProductResponseDto response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));

        verify(productRepository, times(1)).findById(99L);
    }
}