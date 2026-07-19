package org.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;
}
