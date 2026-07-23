package org.example.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String categoryName;
    private String imageUrl;
}
